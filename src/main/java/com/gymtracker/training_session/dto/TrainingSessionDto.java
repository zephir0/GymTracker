package com.gymtracker.training_session.dto;

import com.gymtracker.training_log.dto.TrainingLogDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TrainingSessionDto(@NotNull Long trainingRoutineId, @NotNull List<TrainingLogDto> trainingLogDtoList,
                                 @NotNull LocalDateTime trainingDate) {
}

