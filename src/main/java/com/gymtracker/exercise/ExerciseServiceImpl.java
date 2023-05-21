package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.exercise.exception.NotAuthorizedToAccessExerciseException;
import com.gymtracker.user.UserService;
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
        exerciseRepository.save(exercise);
    }

    @Override
    public void editExercise(Long id,
                             ExerciseDto exerciseDto) {
        exerciseRepository.findById(id).filter(this::checkAuthorization).ifPresentOrElse(exercise -> {
            exercise.setDescription(exerciseDto.description());
            exercise.setMuscleGroup(exerciseDto.muscleGroup());
            exerciseRepository.save(exercise);
        }, () -> {
            throw new ExerciseNotFoundException("Exercise not found");
        });
    }


    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.findById(id).filter(this::checkAuthorization).ifPresentOrElse(exerciseRepository::delete, () -> {
            throw new ExerciseNotFoundException("Exercise not found");
        });
    }


    @Override
    public List<ExerciseResponseDto> getAllExercises() {
        Long userId = userService.getLoggedUser().getId();
        return exerciseRepository.findAllByUserIdOrAdminCreated(userId, true).stream().map(exerciseMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public boolean existById(Long id) {
        return exerciseRepository.existsById(id);
    }


    private boolean checkAuthorization(Exercise exercise) {
        if (!isAuthorized(exercise)) {
            throw new NotAuthorizedToAccessExerciseException("You are not a training session creator or admin");
        } else return true;
    }

    @Override
    public boolean isAuthorized(Exercise exercise) {
        return userService.getLoggedUser().getUserRole().equals(UserRoles.ADMIN) || exercise.getUser().getId().equals(userService.getLoggedUser().getId());
    }
}
