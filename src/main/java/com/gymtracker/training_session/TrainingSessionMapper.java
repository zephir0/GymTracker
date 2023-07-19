package com.gymtracker.training_session;

import com.gymtracker.user.entity.User;

public interface TrainingSessionMapper {

    TrainingSession toEntity(TrainingSessionDto trainingSessionDto,
                             User user);

    TrainingSessionResponseDto toDto(TrainingSession trainingSession);

    TrainingSessionResponseDto toDto(TrainingSession trainingSession,
                                     Long totalWeight);


}

