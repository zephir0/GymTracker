package com.gymtracker.progress_tracker;

import com.gymtracker.training_log.dto.TrainingLogResponseDto;

import java.util.List;

public interface ProgressTrackerService {
    List<TrainingLogResponseDto> sortByMaxWeightAndRepsForExercise(Long exerciseId);

    Long calculateTotalWeightForSession(Long trainingSessionId);

    Long calculateExerciseWeightForSession(Long exerciseId,
                                           Long trainingSessionId);

    Long countTrainingSessions();
}
