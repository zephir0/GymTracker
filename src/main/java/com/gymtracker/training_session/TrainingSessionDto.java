package com.gymtracker.training_session;

import com.gymtracker.training_log.TrainingLogDto;
import org.springframework.lang.NonNull;

import java.util.List;

public record TrainingSessionDto(@NonNull Long trainingRoutineId, List<TrainingLogDto> trainingLogDtoList) {
}

