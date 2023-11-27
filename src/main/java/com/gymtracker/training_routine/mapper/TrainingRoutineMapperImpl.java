package com.gymtracker.training_routine.mapper;

import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_routine.TrainingRoutineDto;
import com.gymtracker.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TrainingRoutineMapperImpl implements TrainingRoutineMapper {
    @Override
    public TrainingRoutine toEntity(TrainingRoutineDto trainingRoutineDto,
                                    User user) {
        TrainingRoutine trainingRoutine = new TrainingRoutine();
        trainingRoutine.setUser(user);
        trainingRoutine.setArchived(false);
        trainingRoutine.setRoutineName(trainingRoutineDto.routineName());
        return trainingRoutine;
    }

}