//package com.gymtracker.training_session;
//
//import com.gymtracker.training_log.TrainingLogMapper;
//import com.gymtracker.training_log.dto.TrainingLogDto;
//import com.gymtracker.training_log.service.TrainingLogService;
//import com.gymtracker.training_session.dto.TrainingSessionDto;
//import com.gymtracker.training_session.service.TrainingSessionService;
//import com.gymtracker.training_session.service.TrainingSessionSyncService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class TrainingSessionSyncServiceTest {
//
//    @Mock
//    private TrainingSessionRepository trainingSessionRepository;
//
//    @Mock
//    private TrainingSessionService trainingSessionService;
//
//    @Mock
//    private TrainingLogMapper trainingLogMapper;
//
//    @Mock
//    private TrainingLogService trainingLogService;
//
//    @InjectMocks
//    private TrainingSessionSyncService trainingSessionSyncService;
//
//    private TrainingSession existingTrainingSession;
//    private TrainingSessionDto trainingSessionDto;
//
//    @Before
//    public void setUp() {
//        existingTrainingSession = new TrainingSession();
//        existingTrainingSession.setId(1L);
//
//        trainingSessionDto = new TrainingSessionDto(1L, new ArrayList<>(), LocalDateTime.now());
//    }
//
//    @Test
//    public void testSynchronizeTrainingSessions() {
//        // Arrange
//        List<TrainingSessionDto> trainingSessionDtoList = new ArrayList<>();
//        trainingSessionDtoList.add(trainingSessionDto);
//
//        when(trainingSessionRepository.findAll()).thenReturn(new ArrayList<>());
//        when(trainingSessionService.createTrainingSession(trainingSessionDto)).thenReturn(existingTrainingSession);
//
//        // Act
//        trainingSessionSyncService.synchronizeTrainingSessions(trainingSessionDtoList);
//
//        // Assert
//        verify(trainingSessionRepository, times(1)).deleteAll(anyList());
//        verify(trainingSessionRepository, times(1)).save(any());
//        verify(trainingLogService, times(1)).editTrainingLog(anyLong(), any(TrainingLogDto.class));
//        verify(trainingLogService, times(1)).createTrainingLogs(anyList(), anyLong());
//    }
//
//    @Test
//    public void testSynchronizeTrainingSessionsWithExistingSession() {
//        // Arrange
//        List<TrainingSessionDto> trainingSessionDtoList = new ArrayList<>();
//        trainingSessionDtoList.add(trainingSessionDto);
//
//        List<TrainingSession> existingSessions = new ArrayList<>();
//        existingSessions.add(existingTrainingSession);
//
//        when(trainingSessionRepository.findAll()).thenReturn(existingSessions);
//        when(trainingSessionRepository.save(any())).thenReturn(existingSessions);
////        when(trainingLogService.editTrainingLog(anyLong(), any(TrainingLogDto.class))).thenReturn(existingTrainingSession.getTrainingLogs().get(0));
//
//        // Act
//        trainingSessionSyncService.synchronizeTrainingSessions(trainingSessionDtoList);
//
//        // Assert
//        verify(trainingSessionRepository, times(1)).deleteAll(anyList());
//        verify(trainingSessionRepository, times(1)).save(any());
//        verify(trainingLogService, times(1)).editTrainingLog(anyLong(), any(TrainingLogDto.class));
//        verify(trainingLogService, never()).createTrainingLogs(anyList(), anyLong());
//    }
//
//    @Test
//    public void testSynchronizeTrainingSessionsWithNoObsoleteSessions() {
//        // Arrange
//        List<TrainingSessionDto> trainingSessionDtoList = new ArrayList<>();
//        trainingSessionDtoList.add(trainingSessionDto);
//
//        List<TrainingSession> existingSessions = new ArrayList<>();
//        existingSessions.add(existingTrainingSession);
//
//        when(trainingSessionRepository.findAll()).thenReturn(existingSessions);
//
//        // Act
//        trainingSessionSyncService.synchronizeTrainingSessions(trainingSessionDtoList);
//
//        // Assert
//        verify(trainingSessionRepository, never()).deleteAll(anyList());
//        verify(trainingSessionRepository, times(1)).save(any());
//        verify(trainingLogService, times(1)).editTrainingLog(anyLong(), any(TrainingLogDto.class));
//        verify(trainingLogService, never()).createTrainingLogs(anyList(), anyLong());
//    }
//}
