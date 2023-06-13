package com.gymtracker.training_log;

import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionService;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TrainingLogServiceTest {

    private final Long trainingSessionId = 1L;
    private TrainingLogServiceImpl trainingLogService;
    @Mock
    private TrainingLogRepository trainingLogRepository;
    @Mock
    private TrainingLogMapper trainingLogMapper;
    @Mock
    private TrainingSessionService trainingSessionService;
    @Mock
    private ExerciseService exerciseService;
    private TrainingLogDto trainingLogDto;
    private TrainingSession trainingSession;
    private TrainingLog trainingLog;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingLogService = new TrainingLogServiceImpl(trainingLogRepository, trainingLogMapper, trainingSessionService, exerciseService);
        trainingLog = new TrainingLog();

        trainingLogDto = new TrainingLogDto(10, 10, "Some notes", 1L, 1L);
        trainingSession = mock(TrainingSession.class);
        trainingSession.setId(trainingSessionId);
    }

    @Test
    public void testCreateTrainingLog_ExerciseNotFound() {
        mockCommonBehaviors(false, Optional.empty(), true);
        assertThrows(ExerciseNotFoundException.class, () -> trainingLogService.createTrainingLog(trainingLogDto));
        verify(trainingLogRepository, never()).save(any(TrainingLog.class));
    }

    @Test
    public void testCreateTrainingLog_TrainingSessionNotFound() {
        mockCommonBehaviors(true, Optional.empty(), true);

        assertThrows(TrainingSessionNotFoundException.class, () -> trainingLogService.createTrainingLog(trainingLogDto));
        verify(trainingLogRepository, never()).save(any(TrainingLog.class));
    }

    @Test
    public void testCreateTrainingLog_UnauthorizedAccess() {
        mockCommonBehaviors(true, Optional.of(trainingSession), false);

        assertThrows(UnauthorizedTrainingSessionAccessException.class, () -> trainingLogService.createTrainingLog(trainingLogDto));
        verify(trainingLogRepository, never()).save(any(TrainingLog.class));
    }

    @Test
    public void testCreateTrainingLog_Success() {
        mockCommonBehaviors(true, Optional.of(trainingSession), true);
        when(trainingLogMapper.toEntity(trainingLogDto)).thenReturn(trainingLog);
        trainingLogService.createTrainingLog(trainingLogDto);

        verify(trainingLogRepository, times(1)).save(any(TrainingLog.class));
    }

    private void mockCommonBehaviors(boolean doesExerciseExist,
                                     Optional<TrainingSession> trainingSessionOpt,
                                     boolean isSessionAuthorized) {
        when(exerciseService.existById(trainingSessionId)).thenReturn(doesExerciseExist);
        when(trainingSessionService.getTrainingSessionById(trainingSessionId)).thenReturn(trainingSessionOpt);
        when(trainingSessionService.isAuthorized(trainingSession)).thenReturn(isSessionAuthorized);
    }

    @Test
    public void testDeleteTrainingLog_TrainingLogNotFound() {
        when(trainingLogRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingLogNotFoundException.class, () -> trainingLogService.deleteTrainingLog(1L));
    }

    @Test
    public void testDeleteTrainingLog_Success() {
        trainingLog.setTrainingSession(trainingSession);
        when(trainingLogRepository.findById(anyLong())).thenReturn(Optional.of(trainingLog));
        when(trainingSessionService.isAuthorized(any(TrainingSession.class))).thenReturn(true);

        assertDoesNotThrow(() -> trainingLogService.deleteTrainingLog(1L));
        verify(trainingLogRepository, times(1)).deleteById(trainingLog.getId());
    }

    @Test
    public void testEditTrainingLog_TrainingLogNotFound() {
        when(trainingLogRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingLogNotFoundException.class, () -> trainingLogService.editTrainingLog(1L, trainingLogDto));
    }

    @Test
    public void testEditTrainingLog_Success() {
        when(trainingLogRepository.findById(anyLong())).thenReturn(Optional.of(trainingLog));
        when(trainingSessionService.isAuthorized(trainingLog.getTrainingSession())).thenReturn(true);

        assertDoesNotThrow(() -> trainingLogService.editTrainingLog(1L, trainingLogDto));
        verify(trainingLogRepository, times(1)).save(any(TrainingLog.class));
    }

    @Test
    public void testGetTrainingLogsForTrainingSession_TrainingSessionNotFound() {
        when(trainingSessionService.getTrainingSessionById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingSessionNotFoundException.class, () -> trainingLogService.getTrainingLogsForTrainingSession(1L));
    }

    @Test
    public void testGetAllByExerciseId_Success() {
        List<TrainingLog> trainingLogs = new ArrayList<>();
        when(trainingLogRepository.findAllByExerciseId(anyLong())).thenReturn(trainingLogs);

        assertEquals(trainingLogs, trainingLogService.getAllByExerciseId(1L));
    }

    @Test
    public void testGetAllByTrainingSessionId_TrainingSessionNotFound() {
        when(trainingSessionService.getTrainingSessionById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingSessionNotFoundException.class, () -> trainingLogService.getAllByTrainingSessionId(1L));
    }

    @Test
    public void testGetAllByExerciseIdAndTrainingSessionId_TrainingSessionNotFound() {
        when(trainingSessionService.getTrainingSessionById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingSessionNotFoundException.class, () -> trainingLogService.getAllByExerciseIdAndTrainingSessionId(1L, 1L));
    }

    @Test
    public void testGetAllByExerciseIdAndTrainingSessionId_ExerciseNotFound() {
        when(trainingSessionService.getTrainingSessionById(anyLong())).thenReturn(Optional.of(new TrainingSession()));
        when(trainingSessionService.isAuthorized(any(TrainingSession.class))).thenReturn(true);
        doThrow(new ExerciseNotFoundException("Exercise not found")).when(exerciseService).existById(anyLong());

        assertThrows(ExerciseNotFoundException.class, () -> trainingLogService.getAllByExerciseIdAndTrainingSessionId(1L, 1L));
    }

    @Test
    public void testGetAllByExerciseIdAndTrainingSessionId_Success() {
        when(trainingSessionService.getTrainingSessionById(anyLong())).thenReturn(Optional.of(trainingSession));
        when(trainingSessionService.isAuthorized(trainingSession)).thenReturn(true);
        List<TrainingLog> trainingLogs = new ArrayList<>();

        assertEquals(trainingLogs, trainingLogService.getAllByExerciseIdAndTrainingSessionId(1L, 1L));
    }

}