package com.gymtracker.progress_tracker;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_session.TrainingSessionRepository;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProgressTrackerServiceTest {

    @Mock
    private TrainingLogService trainingLogService;
    @Mock
    private TrainingSessionRepository trainingSessionRepository;
    @Mock
    private TrainingLogMapper trainingLogMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private ProgressTrackerServiceImpl progressTrackerService;

    private List<TrainingLog> trainingLogs;
    private User loggedUser;

    @Before
    public void setUp() {
        TrainingLog trainingLog = new TrainingLog();
        trainingLog.setWeight(100);
        trainingLog.setReps(10);
        trainingLogs = List.of(trainingLog);

        loggedUser = new User();
    }

    @Test
    public void testSortByMaxWeightAndRepsForExercise() {
        // Arrange
        TrainingLog trainingLog1 = new TrainingLog();
        trainingLog1.setWeight(100);
        trainingLog1.setReps(10);

        TrainingLog trainingLog2 = new TrainingLog();
        trainingLog2.setWeight(200);
        trainingLog2.setReps(8);

        TrainingLog trainingLog3 = new TrainingLog();
        trainingLog3.setWeight(200);
        trainingLog3.setReps(12);

        trainingLogs = List.of(trainingLog1, trainingLog2, trainingLog3);
        when(trainingLogService.getAllByExerciseId(anyLong())).thenReturn(trainingLogs);
        when(trainingLogMapper.toDto(any())).thenAnswer(invocation -> {
            TrainingLog log = invocation.getArgument(0);
            return new TrainingLogResponseDto(1L, "test", log.getReps().longValue(), log.getWeight().longValue(), "someNotes");
        });

        // Act
        List<TrainingLogResponseDto> result = progressTrackerService.sortByMaxWeightAndRepsForExercise(1L);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(200, result.get(0).weight());
        assertEquals(12, result.get(0).reps());
        assertEquals(200, result.get(1).weight());
        assertEquals(8, result.get(1).reps());
        assertEquals(100, result.get(2).weight());
        assertEquals(10, result.get(2).reps());
    }


    @Test
    public void testCalculateTotalWeightForSession() {
        // Arrange
        when(trainingLogService.getAllByTrainingSessionId(anyLong())).thenReturn(trainingLogs);

        // Act
        Long result = progressTrackerService.calculateTotalWeightForSession(1L);

        // Assert
        assertEquals(100, result);
    }

    @Test
    public void testCalculateExerciseWeightForSession() {
        // Arrange
        when(trainingLogService.getAllByExerciseIdAndTrainingSessionId(anyLong(), anyLong())).thenReturn(trainingLogs);

        // Act
        Long result = progressTrackerService.calculateExerciseWeightForSession(1L, 1L);

        // Assert
        assertEquals(100, result);
    }

    @Test
    public void testCountTrainingSessions() {
        // Arrange
        when(userService.getLoggedUser()).thenReturn(loggedUser);
        when(trainingSessionRepository.countByUser(loggedUser)).thenReturn(10L);

        // Act
        Long result = progressTrackerService.countTrainingSessions();

        // Assert
        assertEquals(10, result);
    }
}
