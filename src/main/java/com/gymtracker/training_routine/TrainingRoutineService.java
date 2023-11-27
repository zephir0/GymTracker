package com.gymtracker.training_routine;

import com.gymtracker.exercise.ExerciseRepository;
import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_routine.exception.TrainingRoutineNotFoundException;
import com.gymtracker.training_routine.exception.UnauthorizedTrainingRoutineAccessException;
import com.gymtracker.training_routine.mapper.TrainingRoutineMapper;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import com.gymtracker.user.service.UserService;
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

    public TrainingRoutine getTrainingRoutine(Long trainingRoutineId) {
        return trainingRoutineRepository.findByTrainingRoutineId(trainingRoutineId)
                .map(this::verifyUserAuthorizedForTrainingRoutine)
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

        trainingRoutineDto.exerciseList()
                .stream()
                .map(exerciseDto -> createAndSaveExercise(exerciseDto, loggedUser))
                .filter(exercise -> routineHasNotExercise(routine, exercise))
                .forEach(routine::addExercise);

        trainingRoutineRepository.save(routine);
    }

    @Transactional
    public void archiveTrainingRoutine(Long trainingRoutineId) {
        trainingRoutineRepository.findById(trainingRoutineId)
                .map(trainingRoutine -> {
                    verifyUserAuthorizedForTrainingRoutine(trainingRoutine);
                    trainingRoutine.setArchived(true);
                    return trainingRoutineRepository.save(trainingRoutine);
                })
                .orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine not found."));
    }


    public Map<Long, TrainingLogResponseDto> getPreviousTrainingEntries(Long trainingRoutineId) {
        TrainingRoutine trainingRoutine = trainingRoutineRepository.findById(trainingRoutineId)
                .map(this::verifyUserAuthorizedForTrainingRoutine)
                .orElseThrow(() -> new TrainingRoutineNotFoundException("Training routine not found"));


        Map<Long, TrainingLogResponseDto> previousTrainingEntriesMap = new HashMap<>();

        trainingRoutine.getExerciseList()
                .stream()
                .filter(exercise -> !trainingLogService.getAllByExerciseId(exercise.getId()).isEmpty())
                .forEach(exercise ->
                        addLatestTrainingLog(previousTrainingEntriesMap, exercise)
                );

        return previousTrainingEntriesMap;
    }


    private Exercise createAndSaveExercise(ExerciseDto exerciseDto,
                                           User user) {
        return exerciseRepository.findByExerciseNameAndUserIdOrAdminCreated(exerciseDto.name(), user.getId(), true)
                .orElseGet(() -> {
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


    private TrainingRoutine verifyUserAuthorizedForTrainingRoutine(TrainingRoutine trainingRoutine) {
        if (isUserAuthorizedForTrainingRoutine(trainingRoutine)) {
            return trainingRoutine;
        } else {
            throw new UnauthorizedTrainingRoutineAccessException("You are not authorized to access that training routine");
        }
    }

    private boolean isUserAuthorizedForTrainingRoutine(TrainingRoutine trainingRoutine) {
        User loggedUser = userService.getLoggedUser();
        return loggedUser.getId().equals(trainingRoutine.getUser().getId());
    }
}
