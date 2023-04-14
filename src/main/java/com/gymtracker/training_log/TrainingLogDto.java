package com.gymtracker.training_log;


import org.springframework.lang.NonNull;

public record TrainingLogDto(@NonNull Integer reps,
                             Integer weight,
                             @NonNull String personalNotes,
                             @NonNull Long exerciseId,
                             @NonNull Long trainingSessionId) {
}
