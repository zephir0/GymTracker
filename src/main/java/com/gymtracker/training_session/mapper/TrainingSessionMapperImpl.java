package com.gymtracker.training_session.mapper;

import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.progress_tracker.ProgressTrackerService;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;
import com.gymtracker.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@Component
public class TrainingSessionMapperImpl implements TrainingSessionMapper {
    private final ProgressTrackerService progressTrackerService;

    @Override
    public TrainingSession toEntity(TrainingRoutine trainingRoutine,
                                    User user) {
        TrainingSession trainingSession = new TrainingSession();


        Optional.ofNullable(user)
                .ifPresent(u -> updateTrainingSessionFromUser(u, trainingSession));


        trainingSession.setTrainingRoutine(trainingRoutine);


        return trainingSession;
    }

    @Override
    public TrainingSessionResponseDto toDto(TrainingSession trainingSession) {
        if (trainingSession == null) {
            return null;
        }

        Long totalWeight = progressTrackerService.calculateTotalWeightForSession(trainingSession.getId());
        return new TrainingSessionResponseDto(
                trainingSession.getId(), trainingSession.getTrainingRoutine().getRoutineName(),
                trainingSession.getTrainingDate(),
                totalWeight
        );
    }

    @Override
    public TrainingSessionResponseDto toDto(TrainingSession trainingSession,
                                            Long totalWeight) {
        if (trainingSession == null) {
            return null;
        }

        return new TrainingSessionResponseDto(
                trainingSession.getId(), trainingSession.getTrainingRoutine().getRoutineName(),
                trainingSession.getTrainingDate(),
                totalWeight
        );
    }


    private void updateTrainingSessionFromUser(User user,
                                               TrainingSession trainingSession) {
        trainingSession.setUser(user);
    }
}
