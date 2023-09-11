package com.gymtracker.training_routine.mapper;

import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_routine.TrainingRoutineDto;
import com.gymtracker.user.entity.User;

public interface TrainingRoutineMapper {

    TrainingRoutineDto toDto(TrainingRoutine trainingRoutine);

    TrainingRoutine toEntity(TrainingRoutineDto trainingRoutineDto,
                             User user);

}
