package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.ExerciseAlreadyCreatedByAdminException;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.NotAuthorizedToAccessExerciseException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;


    @Test
    public void testCreateExercise_Success() {
        // Arrange
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        User loggedUser = new User();
        when(userService.getLoggedUser()).thenReturn(loggedUser);

        Exercise exercise = new Exercise();
        when(exerciseMapper.toEntity(exerciseDto, loggedUser)).thenReturn(exercise);

        // Act
        exerciseService.createExercise(exerciseDto);

        // Assert
        verify(exerciseRepository, times(1)).findByNameAndAdminCreated(exercise.getName(), true);
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    public void testCreateExercise_AlreadyCreatedByAdmin() {
        // Arrange
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        User loggedUser = new User();
        when(userService.getLoggedUser()).thenReturn(loggedUser);

        Exercise exercise = new Exercise();
        exercise.setName("ExerciseName");
        when(exerciseMapper.toEntity(exerciseDto, loggedUser)).thenReturn(exercise);

        Exercise existingExercise = new Exercise();
        existingExercise.setName("ExerciseName");
        when(exerciseRepository.findByNameAndAdminCreated("ExerciseName", true)).thenReturn(Optional.of(existingExercise));

        // Act & Assert
        assertThrows(ExerciseAlreadyCreatedByAdminException.class, () -> exerciseService.createExercise(exerciseDto));
        verify(exerciseRepository, never()).save(exercise);
    }


    @Test
    public void testEditExercise_Success() {
        // Arrange
        UserRoles userRoles = UserRoles.ADMIN;
        Long exerciseId = 1L;
        User user = new User();
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        Exercise existingExercise = new Exercise();
        user.setUserRole(userRoles);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(userService.getLoggedUser()).thenReturn(user);
        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);
        // Assert
        verify(exerciseRepository, times(1)).save(existingExercise);
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void testEditExercise_ExerciseNotFound() {
        // Arrange
        Long exerciseId = 1L;
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);
    }

    @Test(expected = NotAuthorizedToAccessExerciseException.class)
    public void testEditExercise_NotAuthorized() {
        // Arrange
        exerciseService = spy(exerciseService);

        Long exerciseId = 1L;
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        Exercise existingExercise = new Exercise();
        User user = new User();
        user.setUserRole(UserRoles.USER);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        doReturn(false).when(exerciseService).isAuthorized(existingExercise);

        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);
    }

    @Test
    public void testDeleteExercise_Success() {
        // Arrange
        Long exerciseId = 1L;
        User user = new User();
        user.setId(1L);
        user.setUserRole(UserRoles.USER);

        Exercise existingExercise = new Exercise();
        existingExercise.setUser(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(userService.getLoggedUser()).thenReturn(user);

        // Act
        exerciseService.deleteExercise(exerciseId);

        // Assert
        verify(exerciseRepository, times(1)).delete(existingExercise);
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void testDeleteExercise_ExerciseNotFound() {
        // Arrange
        Long exerciseId = 1L;
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        // Act
        exerciseService.deleteExercise(exerciseId);
    }

    @Test(expected = NotAuthorizedToAccessExerciseException.class)
    public void testDeleteExercise_NotAuthorized() {
        // Arrange
        Long exerciseId = 1L;
        exerciseService = spy(exerciseService);
        Exercise existingExercise = new Exercise();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));

        doReturn(false).when(exerciseService).isAuthorized(existingExercise);

        // Act
        exerciseService.deleteExercise(exerciseId);
    }

    @Test
    public void testGetAllExercises_Success() {
        // Given
        User loggedUser = new User();
        when(userService.getLoggedUser()).thenReturn(loggedUser);

        // When
        List<ExerciseResponseDto> result = exerciseService.getAllExercises();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testExistById_Exercise_EXIST() {
        // Given
        Long exerciseId = 1L;
        when(exerciseRepository.existsById(exerciseId)).thenReturn(true);

        // When
        boolean result = exerciseService.existById(exerciseId);

        // Then
        assertTrue(result);
    }

    @Test
    public void testExistById_Exercise_NOT_EXIST() {
        // Given
        Long exerciseId = 1L;
        when(exerciseRepository.existsById(exerciseId)).thenReturn(false);

        // When
        boolean result = exerciseService.existById(exerciseId);

        // Then
        assertFalse(result);
    }
}
