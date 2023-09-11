package com.gymtracker.exercise;

import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.dto.ExerciseResponseDto;
import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.exercise.entity.MuscleGroup;
import com.gymtracker.exercise.exception.ExerciseAlreadyCreatedByAdminException;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.UnauthorizedExerciseAccessException;
import com.gymtracker.exercise.service.ExerciseServiceImpl;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.junit.Before;
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

    private ExerciseDto exerciseDto;
    private User loggedUser;
    private Exercise exercise;
    private Exercise existingExercise;
    private Long exerciseId;

    @Before
    public void setUp() {
        exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);
        loggedUser = new User();
        exercise = new Exercise();
        existingExercise = new Exercise();
        exerciseId = 1L;

        when(userService.getLoggedUser()).thenReturn(loggedUser);
        when(exerciseMapper.toEntity(exerciseDto, loggedUser)).thenReturn(exercise);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
    }

    @Test
    public void testCreateExercise_Success() {
        // Arrange
        when(exerciseRepository.findByNameAndAdminCreated(exercise.getName(), true)).thenReturn(Optional.empty());

        // Act
        exerciseService.createExercise(exerciseDto);

        // Assert
        verify(exerciseRepository, times(1)).findByNameAndAdminCreated(exercise.getName(), true);
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test(expected = ExerciseAlreadyCreatedByAdminException.class)
    public void testCreateExercise_AlreadyCreatedByAdmin() {
        // Arrange
        exercise.setName("ExerciseName");
        existingExercise.setName("ExerciseName");

        when(userService.getLoggedUser()).thenReturn(loggedUser);
        when(exerciseMapper.toEntity(exerciseDto, loggedUser)).thenReturn(exercise);
        when(exerciseRepository.findByNameAndAdminCreated("ExerciseName", true)).thenReturn(Optional.of(existingExercise));

        // Act & Assert
        exerciseService.createExercise(exerciseDto);
        verify(exerciseRepository, never()).save(exercise);
    }

    @Test
    public void testEditExercise_Success() {
        // Arrange
        UserRoles userRoles = UserRoles.ADMIN;
        User user = new User();
        ExerciseDto exerciseDto = new ExerciseDto(1L, "TEST", MuscleGroup.CHEST);

        user.setUserRole(userRoles);
        when(userService.getLoggedUser()).thenReturn(user);

        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);

        // Assert
        verify(exerciseRepository, times(1)).save(existingExercise);
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void testEditExercise_ExerciseNotFound() {
        // Arrange
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);
    }

    @Test(expected = UnauthorizedExerciseAccessException.class)
    public void testEditExercise_Unauthorized() {
        // Arrange
        exerciseService = spy(exerciseService);
        existingExercise.setUser(new User());
        doReturn(false).when(exerciseService).isAuthorized(existingExercise);

        // Act
        exerciseService.editExercise(exerciseId, exerciseDto);
    }

    @Test
    public void testDeleteExercise_Success() {
        // Arrange
        loggedUser.setId(1L);
        loggedUser.setUserRole(UserRoles.USER);
        existingExercise.setUser(loggedUser);
        when(userService.getLoggedUser()).thenReturn(loggedUser);

        // Act
        exerciseService.deleteExercise(exerciseId);

        // Assert
        verify(exerciseRepository, times(1)).delete(existingExercise);
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void shouldDeleteExercise_ExerciseNotFound() {
        // Arrange
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        // Act
        exerciseService.deleteExercise(exerciseId);
    }

    @Test(expected = UnauthorizedExerciseAccessException.class)
    public void testDeleteExercise_Unauthorized() {
        // Arrange
        exerciseService = spy(exerciseService);
        existingExercise.setUser(new User());
        doReturn(false).when(exerciseService).isAuthorized(existingExercise);

        // Act
        exerciseService.deleteExercise(exerciseId);
    }

    @Test
    public void testGetAllExercises_Success() {
        // Arrange
        List<ExerciseResponseDto> result = exerciseService.getAllExercises();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testIsAuthorized_Admin_Success() {
        // Arrange
        loggedUser.setUserRole(UserRoles.ADMIN);

        // Act && Assert
        assertTrue(exerciseService.isAuthorized(exercise));
    }

    @Test
    public void testIsAuthorized_User_Success() {
        // Arrange
        loggedUser.setUserRole(UserRoles.USER);
        loggedUser.setId(1L);
        exercise.setUser(loggedUser);

        // Act && Assert
        assertTrue(exerciseService.isAuthorized(exercise));
    }

    @Test
    public void testIsAuthorized_Failed() {
        // Arrange
        loggedUser.setUserRole(UserRoles.USER);
        loggedUser.setId(1L);

        User unauthorizedUser = new User();
        unauthorizedUser.setUserRole(UserRoles.USER);
        unauthorizedUser.setId(2L);

        exercise.setUser(loggedUser);

        when(userService.getLoggedUser()).thenReturn(unauthorizedUser);

        //Act && Assert
        assertFalse(exerciseService.isAuthorized(exercise));
    }


}
