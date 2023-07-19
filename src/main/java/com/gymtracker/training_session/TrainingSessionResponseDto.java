package com.gymtracker.training_session;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TrainingSessionResponseDto(Long id, String routineName,
                                         @JsonFormat(pattern = "dd-MM-yyyy")
                                         LocalDate trainingDate,
                                         Long totalWeight) {

}
