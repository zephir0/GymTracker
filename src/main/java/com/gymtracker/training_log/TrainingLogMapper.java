package com.gymtracker.training_log;

import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingLogMapper {
    @Mapping(source = "trainingLogDto.exerciseId", target = "exercise.id")
    @Mapping(source = "trainingSessionId" ,target = "trainingSession.id" )
    TrainingLog toEntity(TrainingLogDto trainingLogDto, Long trainingSessionId);

    TrainingLogResponseDto toDto(TrainingLog trainingLog);

    TrainingLogResponseDto toDto(TrainingLog trainingLog,
                                 String name);

}
