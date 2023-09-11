package com.gymtracker.training_session.mapper;

import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;
import com.gymtracker.user.entity.User;

public interface TrainingSessionMapper {

    TrainingSession toEntity(TrainingRoutine trainingRoutine,
                             User user);

    TrainingSessionResponseDto toDto(TrainingSession trainingSession);

    TrainingSessionResponseDto toDto(TrainingSession trainingSession,
                                     Long totalWeight);


}

