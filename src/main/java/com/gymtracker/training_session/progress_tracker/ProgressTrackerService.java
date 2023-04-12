package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogService;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressTrackerService {
    private final TrainingLogService trainingLogService;
    private final ExerciseService exerciseService;

    public List<TrainingLog> sortByMaxWeightAndMaxRepForExercise(String exerciseDescription) {
        Exercise referenceByDescription = getExerciseByReference(exerciseDescription);
        List<TrainingLog> trainingLogs = trainingLogService.findAllByExerciseId(referenceByDescription.getId());
        return sortByWeightAndReps(trainingLogs);
    }

    public Integer calculateWeightForTrainingSession(Long trainingSessionId) {
        List<TrainingLog> trainingLogList = trainingLogService.findAllByTrainingSessionId(trainingSessionId);
        return calculateWeight(trainingLogList);
    }

    public Integer calculateTotalWeightForExerciseFromTrainingSession(Long trainingSessionId,
                                                                      String exerciseDescription) {
        Exercise exercise = getExerciseByReference(exerciseDescription);
        List<TrainingLog> trainingLogList = trainingLogService.findAllByExerciseIdAndTrainingSessionId(trainingSessionId, exercise.getId());
        return calculateWeight(trainingLogList);
    }

    private Integer calculateWeight(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().mapToInt(TrainingLog::getWeight).sum();
    }

    private List<TrainingLog> sortByWeightAndReps(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().sorted(Comparator.comparing(TrainingLog::getWeight, Comparator.reverseOrder()).thenComparing(TrainingLog::getReps, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    private Exercise getExerciseByReference(String exerciseDescription) {
        return exerciseService.getReferenceByDescription(exerciseDescription).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found."));
    }

}
