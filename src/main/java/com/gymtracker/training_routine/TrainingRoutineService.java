package com.gymtracker.training_routine;

import com.gymtracker.exercise.Exercise;
import com.gymtracker.exercise.ExerciseDto;
import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingRoutineService {
    private final TrainingRoutineRepository trainingRoutineRepository;
    private final UserService userService;
    private final TrainingRoutineMapper trainingRoutineMapper;
    private final ExerciseRepository exerciseRepository;

    public TrainingRoutine getTrainingRoutine(Long id) {
        return trainingRoutineRepository.findByTrainingRoutineId(id).orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine doesn't exist."));
    }

    public List<TrainingRoutine> getAllTrainingRoutinesForUser() {
        User loggedUser = userService.getLoggedUser();
        return trainingRoutineRepository.findAllByUserId(loggedUser.getId());
    }

    @Transactional
    public void createTrainingRoutine(TrainingRoutineDto trainingRoutineDto) {
        User loggedUser = userService.getLoggedUser();
        TrainingRoutine routine = trainingRoutineMapper.toEntity(trainingRoutineDto, loggedUser);

        trainingRoutineDto.exerciseList().stream()
                .map(exerciseDto -> createAndSaveExercise(exerciseDto, loggedUser))
                .filter(exercise -> routineHasNotExercise(routine, exercise))
                .forEach(routine::addExercise);

        trainingRoutineRepository.save(routine);
    }

    private Exercise createAndSaveExercise(ExerciseDto exerciseDto, User user) {
        return exerciseRepository.findByExerciseNameAndUserIdOrAdminCreated(
                        exerciseDto.name(), user.getId(), true)
                .orElseGet(() -> {
                    Exercise exercise = new Exercise();
                    exercise.setName(exerciseDto.name());
                    exercise.setMuscleGroup(exerciseDto.muscleGroup());
                    exercise.setAdminCreated(user.getUserRole().equals(UserRoles.ADMIN));
                    exercise.setUser(user);
                    return exerciseRepository.save(exercise);
                });
    }

    private boolean routineHasNotExercise(TrainingRoutine routine, Exercise exercise) {
        return routine.getExerciseList().stream()
                .noneMatch(e -> e.getId().equals(exercise.getId()));
    }

}
