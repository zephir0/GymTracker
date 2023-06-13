package com.gymtracker.training_session;

import com.gymtracker.training_session.progress_tracker.ProgressTrackerService;
import com.gymtracker.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TrainingSessionMapperImpl implements TrainingSessionMapper {
    private final ProgressTrackerService progressTrackerService;

    @Override
    public TrainingSession toEntity(TrainingSessionDto trainingSessionDto,
                                    User user) {
        TrainingSession trainingSession = new TrainingSession();


        Optional.ofNullable(user)
                .ifPresent(u -> updateTrainingSessionFromUser(u, trainingSession));

        trainingSession.setTrainingDate(java.time.LocalDateTime.now());

        return trainingSession;
    }

    @Override
    public TrainingSessionResponseDto toDto(TrainingSession trainingSession) {
        if (trainingSession == null) {
            return null;
        }

        Integer totalWeight = progressTrackerService.calculateTotalWeightForSession(trainingSession.getId());
        return new TrainingSessionResponseDto(
                trainingSession.getId(),
                trainingSession.getTrainingDate(),

                totalWeight
        );
    }


    private void updateTrainingSessionFromUser(User user,
                                               TrainingSession trainingSession) {
        trainingSession.setUser(user);
    }
}
