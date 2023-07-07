package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.training_log.TrainingLogResponseDto;

import java.util.List;

public interface ProgressTrackerService {
    List<TrainingLogResponseDto> sortByMaxWeightAndRepsForExercise(Long exerciseId);

    Long calculateTotalWeightForSession(Long trainingSessionId);

    Long calculateExerciseWeightForSession(Long exerciseId,
                                              Long trainingSessionId);
}
