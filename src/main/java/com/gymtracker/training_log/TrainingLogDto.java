package com.gymtracker.training_log;


public record TrainingLogDto(Integer reps, Integer weight, String personalNotes, Long exerciseId, Long trainingSessionId) {
}
