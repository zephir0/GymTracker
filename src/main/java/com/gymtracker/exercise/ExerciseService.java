package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.NotAuthorizedToAccessExerciseException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserService userService;

    public void createExercise(ExerciseDto exerciseDto) {
        User loggedUser = userService.getLoggedUser();
        Exercise exercise = exerciseMapper.toEntity(exerciseDto, loggedUser);
        exerciseRepository.save(exercise);
    }

    public void editExercise(Long id,
                             ExerciseDto exerciseDto) {
        exerciseRepository.findById(id).map(exercise -> {
            if (isAuthorized(exercise)) {
                exercise.setDescription(exerciseDto.description());
                exercise.setMuscleGroup(exerciseDto.muscleGroup());
                exerciseRepository.save(exercise);
                return exercise;
            } else throw new NotAuthorizedToAccessExerciseException("You not admin or creator of that exercise");
        }).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
    }

    public void deleteExercise(Long id) {
        exerciseRepository.findById(id).map(exercise -> {
            if (isAuthorized(exercise)) {
                exerciseRepository.delete(exercise);
                return exercise;
            } else throw new NotAuthorizedToAccessExerciseException("You not admin or creator of that exercise");
        }).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
    }


    public List<ExerciseResponseDto> getAllExercises() {
        Long id = userService.getLoggedUser().getId();
        return exerciseRepository.findAllByUserIdOrAdminCreated(id, true)
                .stream()
                .map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }


    public boolean existById(Long id) {
        return exerciseRepository.existsById(id);
    }


    private boolean isAuthorized(Exercise exercise) {
        return exercise.getUser().equals(userService.getLoggedUser()) || exercise.getUser().getUserRole().equals(UserRoles.ADMIN);
    }


    public Optional<Exercise> getReferenceByDescription(String description) {
        return exerciseRepository.getReferenceByDescription(description);
    }
}
