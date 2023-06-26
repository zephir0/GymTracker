package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.NotAuthorizedToAccessExerciseException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ExerciseServiceTest {

    @InjectMocks
    @Spy
    private ExerciseServiceImpl exerciseService;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserService userService;

    private User user;
    private Exercise exercise;
    private ExerciseDto exerciseDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        exercise = mock(Exercise.class);
        user = mock(User.class);
        exercise.setUser(user);
        user.setId(1L);
        user.setUserRole(UserRoles.ADMIN);
        exerciseDto = new ExerciseDto("name", MuscleGroup.BICEPS);
    }

    @Test
    public void editExercise_exerciseNotFound() {
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseService.editExercise(1L, exerciseDto));
    }

    @Test
    public void editExercise_notAuthorized() {
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.of(exercise));
        doReturn(false).when(exerciseService).isAuthorized(any(Exercise.class));

        assertThrows(NotAuthorizedToAccessExerciseException.class, () -> exerciseService.editExercise(1L, exerciseDto));

    }

    @Test
    public void deleteExercise_exerciseNotFound() {
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ExerciseNotFoundException.class, () -> exerciseService.deleteExercise(1L));
    }

    @Test
    public void deleteExercise_notAuthorized() {
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.of(exercise));
        doReturn(false).when(exerciseService).isAuthorized(any(Exercise.class));

        assertThrows(NotAuthorizedToAccessExerciseException.class, () -> exerciseService.deleteExercise(anyLong()));
    }

    @Test
    public void getAllExercises_emptyList() {
        when(userService.getLoggedUser()).thenReturn(user);
        when(exerciseRepository.findAllByUserIdOrAdminCreated(anyLong(), anyBoolean())).thenReturn(Collections.emptyList());

        List<ExerciseResponseDto> exercises = exerciseService.getAllExercises();

        assertTrue(exercises.isEmpty());
    }

    @Test
    public void getAllExercises_nonEmptyList() {
        when(userService.getLoggedUser()).thenReturn(user);
        when(exerciseRepository.findAllByUserIdOrAdminCreated(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(exercise));

        List<ExerciseResponseDto> exercises = exerciseService.getAllExercises();

        assertFalse(exercises.isEmpty());
        assertEquals(1, exercises.size());
    }

    @Test
    public void existById_true() {
        when(exerciseRepository.existsById(anyLong())).thenReturn(true);

        assertTrue(exerciseService.existById(1L));
    }

    @Test
    public void existById_false() {
        when(exerciseRepository.existsById(anyLong())).thenReturn(false);

        assertFalse(exerciseService.existById(1L));
    }
}
