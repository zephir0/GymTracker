package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.TrainingLogResponseDto;
import com.gymtracker.training_log.TrainingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressTrackerService {
    private final TrainingLogService trainingLogService;
    private final TrainingLogMapper trainingLogMapper;

    public List<TrainingLogResponseDto> sortByMaxWeightAndMaxRepForExercise(Long exerciseId) {
        List<TrainingLog> trainingLogs = trainingLogService.getAllByExerciseId(exerciseId);
        return sortByWeightAndReps(trainingLogs).stream().map(trainingLogMapper::toDto).collect(Collectors.toList());
    }

    public Integer calculateWeightForTrainingSession(Long trainingSessionId) {
        List<TrainingLog> trainingLogList = trainingLogService.getAllByTrainingSessionId(trainingSessionId);
        return calculateWeight(trainingLogList);
    }

    public Integer calculateTotalWeightForExerciseFromTrainingSession(Long exerciseId,
                                                                      Long trainingSessionId) {
        List<TrainingLog> trainingLogList = trainingLogService.getAllByExerciseIdAndTrainingSessionId(exerciseId, trainingSessionId);
        return calculateWeight(trainingLogList);
    }

    private Integer calculateWeight(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().mapToInt(TrainingLog::getWeight).sum();
    }

    private List<TrainingLog> sortByWeightAndReps(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().sorted(Comparator.comparing(TrainingLog::getWeight, Comparator.reverseOrder()).thenComparing(TrainingLog::getReps, Comparator.reverseOrder())).collect(Collectors.toList());
    }


}
