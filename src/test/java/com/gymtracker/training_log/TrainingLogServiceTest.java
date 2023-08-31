package com.gymtracker.training_log;

import com.gymtracker.exercise.Exercise;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionDto;
import com.gymtracker.training_session.TrainingSessionRetriever;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Test
    public void testCreateTrainingLog() {
        // Arrange
        Long sessionId = 1L;
        TrainingLogDto trainingLogDto = new TrainingLogDto(10, 10, "1", 1L, 1L);
        List<TrainingLogDto> trainingLogDtoList = new ArrayList<>();
        trainingLogDtoList.add(trainingLogDto);
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, trainingLogDtoList);
        when(trainingLogMapper.toEntity(trainingLogDto, sessionId)).thenReturn(new TrainingLog());

        // Act
        trainingLogService.createTrainingLogs(trainingSessionDto, sessionId);

        // Assert
        verify(trainingLogRepository, times(1)).save(any(TrainingLog.class));
    }

    @Test
    public void testDeleteTrainingLog_Success() {
        // Arrange
        TrainingLog trainingLog = new TrainingLog();
        trainingLog.setId(1L);
        TrainingSession trainingSession = new TrainingSession();
        trainingLog.setTrainingSession(trainingSession);

        when(trainingLogRepository.findById(trainingLog.getId())).thenReturn(Optional.of(trainingLog));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(true);

        // Act
        trainingLogService.deleteTrainingLog(trainingLog.getId());

        // Assert
        verify(trainingLogRepository, times(1)).deleteById(trainingLog.getId());
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testDeleteTrainingLog_NotFound() {
        // Arrange
        Long trainingLogId = 1L;
        when(trainingLogRepository.findById(trainingLogId)).thenReturn(Optional.empty());

        // Act
        trainingLogService.deleteTrainingLog(trainingLogId);
    }

    @Test(expected = UnauthorizedTrainingSessionAccessException.class)
    public void testDeleteTrainingLog_NotAuthorized() {
        // Arrange
        TrainingLog trainingLog = new TrainingLog();
        TrainingSession trainingSession = new TrainingSession();
        trainingLog.setTrainingSession(trainingSession);

        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(false);

        // Act
        trainingLogService.deleteTrainingLog(1L);
    }

    @Test
    public void testEditTrainingLog_Success() {
        // Arrange
        Long trainingLogId = 1L;
        TrainingLogDto trainingLogDto = new TrainingLogDto(10, 10, "notes", 1L, 1L);
        TrainingLog trainingLog = new TrainingLog();
        TrainingSession trainingSession = new TrainingSession();
        trainingLog.setTrainingSession(trainingSession);

        when(trainingLogRepository.findById(trainingLogId)).thenReturn(Optional.of(trainingLog));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);
        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(true);

        // Act
        trainingLogService.editTrainingLog(trainingLogId, trainingLogDto);

        // Assert
        verify(trainingLogRepository, times(1)).findById(trainingLogId);
        verify(trainingLogRepository, times(1)).save(trainingLog);
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testEditTrainingLog_NotFound() {
        // Arrange
        Long trainingLogId = 1L;
        TrainingLogDto trainingLogDto = new TrainingLogDto(10, 10, "notes", 1L, 1L);

        when(trainingLogRepository.findById(trainingLogId)).thenReturn(Optional.empty());

        // Act
        trainingLogService.editTrainingLog(trainingLogId, trainingLogDto);
    }

    @Test(expected = UnauthorizedTrainingSessionAccessException.class)
    public void testEditTrainingLog_Unauthorized() {
        // Arrange
        TrainingLog trainingLog = new TrainingLog();
        TrainingSession trainingSession = new TrainingSession();
        trainingLog.setTrainingSession(trainingSession);

        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(false);
        when(trainingLogService.checkAuthorization(trainingSession)).thenReturn(false);

        // Act & Assert
        verify(trainingLogRepository, never()).save(trainingLog);
    }

    @Test
    public void testGetTrainingLogsForTrainingSession_Success() {
        // Arrange
        Long trainingSessionId = 1L;
        User user = new User();

        List<TrainingLog> trainingLogList = new ArrayList<>();

        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setUser(user);
        trainingSession.setTrainingLogs(trainingLogList);

        Exercise exercise = new Exercise();
        exercise.setUser(user);

        TrainingLog trainingLog = new TrainingLog();
        trainingLogList.add(trainingLog);
        trainingLog.setTrainingSession(trainingSession);
        trainingLog.setExercise(exercise);


        when(trainingSessionRetriever.getTrainingSessionById(trainingSessionId)).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        // Act
        List<TrainingLogResponseDto> result = trainingLogService.getTrainingLogsForTrainingSession(trainingSessionId);

        // Assert
        assertFalse(result.isEmpty());
        verify(trainingLogMapper, times(1)).toDto(trainingLog, exercise.getName());
    }

    @Test(expected = TrainingSessionNotFoundException.class)
    public void testGetTrainingLogsForTrainingSession_SessionNotFound() {
        // Arrange
        Long trainingSessionId = 1L;

        when(trainingSessionRetriever.getTrainingSessionById(trainingSessionId)).thenReturn(Optional.empty());

        // Act
        trainingLogService.getTrainingLogsForTrainingSession(trainingSessionId);
    }

    @Test(expected = TrainingLogNotFoundException.class)
    public void testGetTrainingLogsForTrainingSession_NoLogs() {
        // Arrange
        Long trainingSessionId = 1L;
        TrainingSession trainingSession = new TrainingSession();

        trainingSession.setTrainingLogs(new ArrayList<>());
        when(trainingSessionRetriever.getTrainingSessionById(trainingSessionId)).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRetriever.isAuthorized(trainingSession)).thenReturn(true);

        // Act & Assert
        trainingLogService.getTrainingLogsForTrainingSession(trainingSessionId);
    }
}