package com.gymtracker.training_routine;

import com.gymtracker.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TrainingRoutineMapperImpl implements TrainingRoutineMapper {
    @Override
    public TrainingRoutineDto toDto(TrainingRoutine trainingRoutine) {
        return null;
    }

    @Override
    public TrainingRoutine toEntity(TrainingRoutineDto trainingRoutineDto,
                                    User user) {
        TrainingRoutine trainingRoutine = new TrainingRoutine();
        trainingRoutine.setUser(user);
        trainingRoutine.setRoutineName(trainingRoutineDto.routineName());
        return trainingRoutine;
    }

}
