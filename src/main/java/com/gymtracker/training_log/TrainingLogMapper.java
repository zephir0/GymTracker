package com.gymtracker.training_log;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingLogMapper {
    @Mapping(source = "exerciseId", target = "exercise.id")
    @Mapping(source = "trainingSessionId", target = "trainingSession.id")
    TrainingLog toEntity(TrainingLogDto trainingLogDto);
}
