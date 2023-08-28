package com.gymtracker.training_routine;

import com.gymtracker.exercise.Exercise;
import com.gymtracker.exercise.ExerciseDto;
import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.TrainingLogResponseDto;
import com.gymtracker.training_log.TrainingLogService;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TrainingRoutineService {
    private final TrainingRoutineRepository trainingRoutineRepository;
    private final UserService userService;
    private final TrainingRoutineMapper trainingRoutineMapper;
    private final ExerciseRepository exerciseRepository;
    private final TrainingLogService trainingLogService;
    private final TrainingLogMapper trainingLogMapper;

    public TrainingRoutine getTrainingRoutine(Long id) {
        return trainingRoutineRepository.findByTrainingRoutineId(id)
                .map(this::isAuthorized)
                .orElseThrow(
                        () -> new TrainingRoutineNotFoundException("Training routine doesn't exist.")
                );
    }

    public List<TrainingRoutine> getTrainingRoutines(boolean isArchived) {
        User loggedUser = userService.getLoggedUser();
        return trainingRoutineRepository.findAllByUserIdAndArchived(loggedUser.getId(), isArchived);
    }

    @Transactional
    public void createTrainingRoutine(TrainingRoutineDto trainingRoutineDto) {
        User loggedUser = userService.getLoggedUser();
        TrainingRoutine routine = trainingRoutineMapper
                .toEntity(trainingRoutineDto, loggedUser);

        trainingRoutineDto.exerciseList().stream()
                .map(exerciseDto -> createAndSaveExercise(exerciseDto, loggedUser))
                .filter(exercise -> routineHasNotExercise(routine, exercise))
                .forEach(routine::addExercise);

        trainingRoutineRepository.save(routine);
    }

    @Transactional
    public void archiveTrainingRoutine(Long routineId) {
        trainingRoutineRepository.findById(routineId)
                .map(trainingRoutine -> {
                    isAuthorized(trainingRoutine);
                    trainingRoutine.setArchived(true);
                    return trainingRoutineRepository.save(trainingRoutine);
                })
                .orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine not found."));
    }


    public Map<Long, TrainingLogResponseDto> getPreviousTrainingEntries(Long routineId) {
        TrainingRoutine trainingRoutine = trainingRoutineRepository.findById(routineId)
                .map(this::isAuthorized)
                .orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine not found"));


        Map<Long, TrainingLogResponseDto> previousTrainingEntriesMap = new HashMap<>();

        trainingRoutine.getExerciseList().stream()
                .filter(exercise -> !trainingLogService.getAllByExerciseId(exercise.getId()).isEmpty())
                .forEach(exercise ->
                        addLatestTrainingLog(previousTrainingEntriesMap, exercise)
                );

        return previousTrainingEntriesMap;
    }


    private Exercise createAndSaveExercise(ExerciseDto exerciseDto,
                                           User user) {
        return exerciseRepository.findByExerciseNameAndUserIdOrAdminCreated(exerciseDto.name(), user.getId(), true).orElseGet(() -> {
            Exercise exercise = new Exercise();
            exercise.setName(exerciseDto.name());
            exercise.setMuscleGroup(exerciseDto.muscleGroup());
            exercise.setAdminCreated(user.getUserRole().equals(UserRoles.ADMIN));
            exercise.setUser(user);
            return exerciseRepository.save(exercise);
        });
    }

    private void addLatestTrainingLog(Map<Long, TrainingLogResponseDto> map,
                                      Exercise exercise) {
        List<TrainingLog> trainingLogs = trainingLogService.getAllByExerciseId(exercise.getId());
        TrainingLog latestTrainingLog = trainingLogs.get(trainingLogs.size() - 1);
        TrainingLogResponseDto trainingLogResponseDto = trainingLogMapper.toDto(latestTrainingLog);
        map.put(exercise.getId(), trainingLogResponseDto);
    }

    private boolean routineHasNotExercise(TrainingRoutine routine,
                                          Exercise exercise) {
        return routine.getExerciseList().stream().noneMatch(e -> e.getId().equals(exercise.getId()));
    }


    private TrainingRoutine isAuthorized(TrainingRoutine trainingRoutine) {
        if (checkAuthorization(trainingRoutine)) {
            return trainingRoutine;
        } else
            throw new NotAuthorizedAccessTrainingRoutineException("You are not authorized to access that training routine");
    }

    private boolean checkAuthorization(TrainingRoutine trainingRoutine) {
        User loggedUser = userService.getLoggedUser();
        return loggedUser.getId().equals(trainingRoutine.getUser().getId());
    }
}
