package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.training_log.TrainingLogResponseDto;

import java.util.List;

public interface ProgressTrackerService {
    List<TrainingLogResponseDto> sortByMaxWeightAndRepsForExercise(Long exerciseId);

    Integer calculateTotalWeightForSession(Long trainingSessionId);

    Integer calculateExerciseWeightForSession(Long exerciseId,
                                              Long trainingSessionId);
}
