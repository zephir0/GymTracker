package com.gymtracker.training_log;


import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

public record TrainingLogDto(@NonNull Integer reps,
                             Integer weight,
                             String personalNotes,
                             @NotNull Long exerciseId,
                             @NotNull Long trainingSessionId) {
}
