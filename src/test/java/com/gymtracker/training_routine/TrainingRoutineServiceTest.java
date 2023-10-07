package com.gymtracker.training_routine;

import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.exercise.entity.MuscleGroup;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_routine.exception.TrainingRoutineNotFoundException;
import com.gymtracker.training_routine.mapper.TrainingRoutineMapper;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrainingRoutineServiceTest {

    @Mock
    private TrainingRoutineRepository trainingRoutineRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainingRoutineMapper trainingRoutineMapper;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private TrainingLogService trainingLogService;
    @Mock
    private TrainingLogMapper trainingLogMapper;

    @InjectMocks
    private TrainingRoutineService trainingRoutineService;

    private User user;
    private TrainingRoutine trainingRoutine;
    private TrainingRoutineDto trainingRoutineDto;
    private Exercise exercise;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);

        exercise = new Exercise();
        exercise.setId(1L);
        ExerciseDto exerciseDto = new ExerciseDto(1L, "Exercise", MuscleGroup.CHEST);
        trainingRoutine = new TrainingRoutine();
        trainingRoutine.setId(1L);
        trainingRoutine.setUser(user);
        trainingRoutine.setExerciseList(List.of(exercise));
        when(userService.getLoggedUser()).thenReturn(user);

        trainingRoutineDto = new TrainingRoutineDto("Test", List.of(exerciseDto));
    }

    @Test
    public void testGetTrainingRoutine_Success() {
        // Arrange
        when(trainingRoutineRepository.findByTrainingRoutineId(anyLong())).thenReturn(Optional.of(trainingRoutine));

        // Act
        TrainingRoutine result = trainingRoutineService.getTrainingRoutine(trainingRoutine.getId());

        // Assert
        assertNotNull(result);
        assertEquals(trainingRoutine.getId(), result.getId());
    }

    @Test(expected = TrainingRoutineNotFoundException.class)
    public void testGetTrainingRoutine_NotFound() {
        // Arrange
        when(trainingRoutineRepository.findByTrainingRoutineId(anyLong())).thenReturn(Optional.empty());

        // Act
        trainingRoutineService.getTrainingRoutine(999L);
    }


    @Test
    public void testCreateTrainingRoutine_Success() {
        // Arrange
        when(trainingRoutineMapper.toEntity(trainingRoutineDto, user)).thenReturn(trainingRoutine);
        when(exerciseRepository.findByExerciseNameAndUserIdOrAdminCreated(anyString(), anyLong(), anyBoolean())).thenReturn(Optional.of(exercise));

        // Act
        trainingRoutineService.createTrainingRoutine(trainingRoutineDto);

        // Assert
        verify(trainingRoutineRepository, times(1)).save(any(TrainingRoutine.class));
    }

    @Test
    public void testGetPreviousTrainingEntries_Success() {
        // Arrange
        TrainingLog trainingLog = new TrainingLog();
        when(trainingRoutineRepository.findById(anyLong())).thenReturn(Optional.of(trainingRoutine));
        when(trainingLogService.getAllByExerciseId(anyLong())).thenReturn(List.of(trainingLog));
        when(trainingLogMapper.toDto(any())).thenReturn(new TrainingLogResponseDto(1L, "Test", 1L, 1L, "someDate"));

        // Act
        Map<Long, TrainingLogResponseDto> result = trainingRoutineService.getPreviousTrainingEntries(trainingRoutine.getId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expected = TrainingRoutineNotFoundException.class)
    public void testGetPreviousTrainingEntries_NotFound() {
        // Arrange
        when(trainingRoutineRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        trainingRoutineService.getPreviousTrainingEntries(999L);
    }
}
