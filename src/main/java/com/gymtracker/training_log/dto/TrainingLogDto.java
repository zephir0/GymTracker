package com.gymtracker.training_log.dto;


import javax.validation.constraints.NotNull;

public record TrainingLogDto(@NotNull Integer reps,
                             Integer weight,
                             String personalNotes,
                             @NotNull Long exerciseId) {
}
