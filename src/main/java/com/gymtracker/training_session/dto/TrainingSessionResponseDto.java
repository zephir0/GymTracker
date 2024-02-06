package com.gymtracker.training_session.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TrainingSessionResponseDto(Long id, String routineName,
                                         @JsonFormat(pattern = "dd-MM-yyyy")
                                         LocalDateTime trainingDate,
                                         Long totalWeight) {
}
