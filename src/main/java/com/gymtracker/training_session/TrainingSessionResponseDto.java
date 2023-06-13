package com.gymtracker.training_session;

import java.time.LocalDateTime;

public record TrainingSessionResponseDto(Long id, LocalDateTime trainingDate,
                                         Integer totalWeight) {

}
