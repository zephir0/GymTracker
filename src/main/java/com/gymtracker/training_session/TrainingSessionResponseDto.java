package com.gymtracker.training_session;

import java.time.LocalDateTime;

public record TrainingSessionResponseDto(LocalDateTime trainingDate, String trainingName) {
}
