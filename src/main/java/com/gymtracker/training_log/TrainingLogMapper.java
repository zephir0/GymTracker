package com.gymtracker.training_log;

import com.gymtracker.training_session.TrainingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingLogMapper {
    @Mapping(source = "exerciseId", target = "exercise.id")
    @Mapping(source = "trainingSessionId", target = "trainingSession.id")
    TrainingLog toEntity(TrainingLogDto trainingLogDto);

    TrainingLogResponseDto toDto(TrainingLog trainingLog);
}
