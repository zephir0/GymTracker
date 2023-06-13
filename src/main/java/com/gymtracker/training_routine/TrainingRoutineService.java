package com.gymtracker.training_routine;

import com.gymtracker.exercise.Exercise;
import com.gymtracker.exercise.ExerciseMapper;
import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class TrainingRoutineService {
    private final TrainingRoutineRepository trainingRoutineRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserService userService;
    private final TrainingRoutineMapper trainingRoutineMapper;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void createTrainingRoutine(TrainingRoutineDto trainingRoutineDto) {
        User loggedUser = userService.getLoggedUser();
        TrainingRoutine routine = trainingRoutineMapper.toEntity(trainingRoutineDto, loggedUser);

        trainingRoutineDto.exerciseList().stream()
                .map(exerciseDto -> exerciseMapper.toEntity(exerciseDto, loggedUser))
                .forEach(exercise -> addExerciseToRoutine(exercise, routine, loggedUser));

        trainingRoutineRepository.save(routine);
    }

    private void addExerciseToRoutine(Exercise exercise,
                                      TrainingRoutine routine,
                                      User user) {
        Exercise savedExercise = findOrCreateExercise(exercise, user);
        if (routineHasNotExercise(routine, savedExercise)) {
            routine.addExercise(savedExercise);
        }
    }

    private Exercise findOrCreateExercise(Exercise exercise,
                                          User user) {
        return exerciseRepository.findByDescriptionAndUserIdOrAdminCreated(
                        exercise.getDescription(), user.getId(), true)
                .orElseGet(() -> createAndSaveExercise(exercise, user));
    }

    private Exercise createAndSaveExercise(Exercise exercise,
                                           User user) {
        Exercise newExercise = new Exercise();
        newExercise.setDescription(exercise.getDescription());
        newExercise.setMuscleGroup(exercise.getMuscleGroup());
        newExercise.setAdminCreated(user.getUserRole().equals(UserRoles.ADMIN));
        newExercise.setUser(user);
        return exerciseRepository.save(newExercise);
    }

    private boolean routineHasNotExercise(TrainingRoutine routine,
                                          Exercise exercise) {
        return routine.getExerciseList().stream()
                .noneMatch(e -> e.getId().equals(exercise.getId()));
    }


    public TrainingRoutine getTrainingRoutine(Long id) {
        return trainingRoutineRepository.findByTrainingRoutineId(id).orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine doesn't exist."));
    }

}
