package com.gymtracker.training_log;

import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_log.service.TrainingLogServiceImpl;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.dto.TrainingSessionDto;
import com.gymtracker.training_session.service.TrainingSessionRetriever;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrainingLogServiceTest {
    @Mock
    private TrainingLogRepository trainingLogRepository;
    @Mock
    private TrainingLogMapper trainingLogMapper;
    @Mock
    private TrainingSessionRetriever trainingSessionRetriever;

    @InjectMocks
    private TrainingLogServiceImpl trainingLogService;

    private Long sessionId;
    private TrainingLogDto trainingLogDto;
    private TrainingSessionDto trainingSessionDto;
    private TrainingLog trainingLog;
    private TrainingSession trainingSession;
    private User user;
    private Exercise exercise;

    @Before
    public void setUp() {
        sessionId = 1L;
        trainingLogDto = new TrainingLogDto(10, 10, "1", 1L);
        trainingSessionDto = new TrainingSessionDto(1L, Collections.singletonList(trainingLogDto), LocalDateTime.now());
        trainingLog = new TrainingLog();
        trainingSession = new TrainingSession();
        user = new User();
        exercise = new Exercise();
        exercise.setId(1L);

        trainingLog.setId(1L);
        trainingLog.setTrainingSession(trainingSession);
        trainingSession.setUser(user);
        trainingSession.setId(1L);
        trainingSession.setTrainingLogs(Collections.singletonList(trainingLog));
        exercise.setUser(user);

        when(trainingLogMapper.toEntity(trainingLogDto, sessionId)).thenReturn(trainingLog);
        when(trainingLogRepository.findById(trainingLog.getId())).thenReturn(Optional.of(trainingLog));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);
        when(trainingSessionRetriever.getTrainingSessionById(sessionId)).thenReturn(Optional.of(trainingSession));
        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(true);
    }

    @Test
    public void testCreateTrainingLog() {
        // Act
        trainingLogService.createTrainingLogs(trainingSessionDto.trainingLogDtoList(), sessionId);

        // Assert
        verify(trainingLogRepository, times(1)).save(any(TrainingLog.class));
    }

    @Test
    public void testDeleteTrainingLog_Success() {
        // Act
        trainingLogService.deleteTrainingLog(trainingLog.getId());

        // Assert
        verify(trainingLogRepository, times(1)).deleteById(trainingLog.getId());
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testDeleteTrainingLog_NotFound() {
        // Arrange
        when(trainingLogRepository.findById(trainingLog.getId())).thenReturn(Optional.empty());

        // Act
        trainingLogService.deleteTrainingLog(trainingLog.getId());
    }

    @Test(expected = UnauthorizedTrainingSessionAccessException.class)
    public void testDeleteTrainingLog_Unauthorized() {
        // Arrange
        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(false);

        // Act
        trainingLogService.deleteTrainingLog(1L);
    }

    @Test
    public void testEditTrainingLog_Success() {
        // Act
        trainingLogService.editTrainingLog(trainingLog.getId(), trainingLogDto);

        // Assert
        verify(trainingLogRepository, times(1)).findById(trainingLog.getId());
        verify(trainingLogRepository, times(1)).save(trainingLog);
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testEditTrainingLog_NotFound() {
        // Arrange
        when(trainingLogRepository.findById(trainingLog.getId())).thenReturn(Optional.empty());

        // Act
        trainingLogService.editTrainingLog(trainingLog.getId(), trainingLogDto);
    }

    @Test(expected = UnauthorizedTrainingSessionAccessException.class)
    public void testEditTrainingLog_Unauthorized() {
        // Arrange
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(false);
        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(false);

        // Act
        trainingLogService.editTrainingLog(trainingLog.getId(), trainingLogDto);

        //Assert
        verify(trainingLogRepository, never()).save(trainingLog);
    }

    @Test
    public void testGetTrainingLogsForTrainingSession_Success() {
        // Arrange
        List<TrainingLog> trainingLogList = new ArrayList<>();

        trainingSession.setTrainingLogs(trainingLogList);

        exercise.setUser(user);

        trainingLogList.add(trainingLog);

        trainingLog.setTrainingSession(trainingSession);
        trainingLog.setExercise(exercise);

        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        // Act
        List<TrainingLogResponseDto> result = trainingLogService.getTrainingLogsForTrainingSession(trainingSession.getId());

        // Assert
        assertFalse(result.isEmpty());
        verify(trainingLogMapper, times(1)).toDto(trainingLog, exercise.getName());
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testGetTrainingLogsForTrainingSession_NotFound() {
        // Arrange
        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId())).thenReturn(Optional.empty());

        // Act
        trainingLogService.getTrainingLogsForTrainingSession(trainingSession.getId());
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testGetTrainingLogsForTrainingSession_NoLogs() {
        // Arrange
        trainingSession.setTrainingLogs(new ArrayList<>());
        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        // Act
        trainingLogService.getTrainingLogsForTrainingSession(trainingSession.getId());
    }

    @Test
    public void testGetAllByExerciseIdAndTrainingSessionId_Success() {
        //Arrange
        List<TrainingLog> trainingLogList = new ArrayList<>();
        trainingLogList.add(trainingLog);
        trainingLog.setExercise(exercise);
        trainingSession.setTrainingLogs(trainingLogList);

        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId())).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        //Act
        List<TrainingLog> result = trainingLogService.getAllByExerciseIdAndTrainingSessionId(exercise.getId(), trainingSession.getId());

        //Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test(expected = UnauthorizedTrainingSessionAccessException.class)
    public void testGetAllByExerciseIdAndTrainingSessionId_Unauthorized() {
        // Arrange
        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId()))
                .thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(false);

        // Act
        trainingLogService.getAllByExerciseIdAndTrainingSessionId(exercise.getId(), trainingSession.getId());
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testGetAllByExerciseIdAndTrainingSessionId_EmptySession() {
        // Arrange
        when(trainingSessionRetriever.getTrainingSessionById(trainingSession.getId()))
                .thenReturn(Optional.empty());

        // Act
        trainingLogService.getAllByExerciseIdAndTrainingSessionId(exercise.getId(), trainingSession.getId());
    }
}
