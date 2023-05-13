package com.gymtracker.training_session;

import com.gymtracker.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TrainingSessionMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "trainingDate", expression = "java(java.time.LocalDateTime.now())")

    TrainingSession toEntity(TrainingSessionDto trainingSessionDto,
                             User user);

    TrainingSessionResponseDto toDto(TrainingSession trainingSession);

}
