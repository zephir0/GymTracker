package com.gymtracker.exercise.service;

import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.exercise.ExerciseMapper;
import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.dto.ExerciseResponseDto;
import com.gymtracker.exercise.exception.ExerciseAlreadyCreatedByAdminException;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.UnauthorizedExerciseAccessException;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserService userService;


    @Override
    public void createExercise(ExerciseDto exerciseDto) {
        User loggedUser = userService.getLoggedUser();
        Exercise exercise = exerciseMapper.toEntity(exerciseDto, loggedUser);

        exerciseRepository.findByNameAndAdminCreated(exercise.getName(), true)
                .ifPresent(existingExercise -> {
                    throw new ExerciseAlreadyCreatedByAdminException(String.format("Exercise with name '%s' was already created by admin.", existingExercise.getName()));
                });

        exerciseRepository.save(exercise);
    }

    @Override
    public void editExercise(Long exerciseId,
                             ExerciseDto exerciseDto) {
        exerciseRepository.findById(exerciseId)
                .filter(this::checkAuthorization)
                .ifPresentOrElse(exercise -> {
                    exercise.setName(exerciseDto.name());
                    exercise.setMuscleGroup(exerciseDto.muscleGroup());
                    exerciseRepository.save(exercise);
                }, () -> {
                    throw new ExerciseNotFoundException("Exercise not found");
                });
    }


    @Override
    public void deleteExercise(Long exerciseId) {
        exerciseRepository.findById(exerciseId)
                .filter(this::checkAuthorization)
                .ifPresentOrElse(exerciseRepository::delete, () -> {
                    throw new ExerciseNotFoundException("Exercise not found");
                });
    }


    @Override
    public List<ExerciseResponseDto> getAllExercises() {
        Long userId = userService.getLoggedUser().getId();
        return exerciseRepository.findAllByUserIdOrAdminCreated(userId, true)
                .stream()
                .map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }


    private boolean checkAuthorization(Exercise exercise) {
        if (!isAuthorized(exercise)) {
            throw new UnauthorizedExerciseAccessException("You are not a training session creator or admin");
        } else return true;
    }

    @Override
    public boolean isAuthorized(Exercise exercise) {
        return userService.getLoggedUser().getUserRole().equals(UserRoles.ADMIN) || exercise.getUser().getId().equals(userService.getLoggedUser().getId());
    }
}
