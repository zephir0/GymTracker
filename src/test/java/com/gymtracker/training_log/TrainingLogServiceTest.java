//package com.gymtracker.training_log;
//
//import com.gymtracker.exercise.ExerciseService;
//import com.gymtracker.training_session.TrainingSession;
//import com.gymtracker.training_session.TrainingSessionService;
//import com.gymtracker.user.UserService;
//import com.gymtracker.user.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingLogServiceTest {
//
//    @InjectMocks
//    private TrainingLogService trainingLogService;
//    @Mock
//    private TrainingSessionService trainingSessionService;
//
//    @Mock
//    private TrainingLogRepository trainingLogRepository;
//
//    @Mock
//    private TrainingLogMapper trainingLogMapper;
//
//
//    @Mock
//    private ExerciseService exerciseService;
//
//    @Mock
//    private UserService userService;
//
//    private TrainingLogDto trainingLogDto;
//    private TrainingLog trainingLog;
//    private TrainingSession trainingSession;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        trainingLogDto = new TrainingLogDto(10, 55, "Personal note", 1L, 1L);
//        trainingLog = new TrainingLog();
//        trainingSession = new TrainingSession();
//        user = new User();
//    }
//
//    @Test
//    void shouldCreateTrainingLog() {
//        when(exerciseService.existById(trainingLogDto.trainingSessionId())).thenReturn(false);
//        when(trainingSessionService.findById(trainingLogDto.trainingSessionId())).thenReturn(trainingSession);
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(any(), any())).thenReturn(true);
//        when(trainingLogMapper.toEntity(trainingLogDto)).thenReturn(trainingLog);
//
//        trainingLogService.createTrainingLog(trainingLogDto);
//
//        verify(trainingLogRepository, times(1)).save(trainingLog);
//    }
//
//    @Test
//    void shouldDeleteTrainingLog() {
//        when(trainingLogRepository.findById(any())).thenReturn(Optional.of(trainingLog));
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
//        trainingLogService.deleteTrainingLog(1L);
//
//        verify(trainingLogRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void shouldEditTrainingLog() {
//        when(trainingLogRepository.findById(any())).thenReturn(Optional.of(trainingLog));
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
//        trainingLogService.editTrainingLog(1L, trainingLogDto);
//
//        verify(trainingLog, times(1)).setReps(trainingLogDto.reps());
//        verify(trainingLog, times(1)).setWeight(trainingLogDto.weight());
//        verify(trainingLog, times(1)).setPersonalNotes(trainingLogDto.personalNotes());
//        verify(trainingLogRepository, times(1)).save(trainingLog);
//    }
//
//    @Test
//    void shouldGetTrainingLogsForTrainingSession() {
//        when(trainingSessionService.findById(any())).thenReturn(trainingSession);
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
////        List<TrainingLog> result = trainingLogService.getTrainingLogsForTrainingSession(1L);
//
//        verify(trainingSession, times(1)).getTrainingLogs();
//        assert (!result.isEmpty());
//        assert (result.size() == 1);
//        assert (result.contains(trainingLog));
//    }
//
//    @Test
//    void shouldFindAllByExerciseId() {
//        when(trainingLogRepository.findAllByExerciseId(any())).thenReturn(Collections.singletonList(trainingLog));
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
//        List<TrainingLog> result = trainingLogService.findAllByExerciseId(1L);
//
//        assert (!result.isEmpty());
//        assert (result.size() == 1);
//        assert (result.contains(trainingLog));
//    }
//
//    @Test
//    void shouldFindAllByTrainingSessionId() {
//        when(trainingLogRepository.findAllByTrainingSessionId(any())).thenReturn(Collections.singletonList(trainingLog));
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
//        List<TrainingLog> result = trainingLogService.findAllByTrainingSessionId(1L);
//
//        assert (!result.isEmpty());
//        assert (result.size() == 1);
//        assert (result.contains(trainingLog));
//    }
//
//    @Test
//    void shouldFindAllByExerciseIdAndTrainingSessionId() {
//        when(trainingLogRepository.findAllByExerciseIdAndTrainingSessionId(any(), any())).thenReturn(Collections.singletonList(trainingLog));
//        when(trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, user)).thenReturn(true);
//
//        List<TrainingLog> result = trainingLogService.findAllByExerciseIdAndTrainingSessionId(1L, 1L);
//
//        assert (!result.isEmpty());
//        assert (result.size() == 1);
//        assert (result.contains(trainingLog));
//    }
//}
