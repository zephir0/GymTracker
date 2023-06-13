package com.gymtracker.training_routine;

import com.gymtracker.user.entity.User;

public interface TrainingRoutineMapper {

    TrainingRoutineDto toDto(TrainingRoutine trainingRoutine);

    TrainingRoutine toEntity(TrainingRoutineDto trainingRoutineDto,
                             User user);

}
