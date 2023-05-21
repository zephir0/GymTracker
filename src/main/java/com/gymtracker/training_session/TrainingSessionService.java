package com.gymtracker.training_session;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionService {
    void createTrainingSession(TrainingSessionDto trainingSessionDto);

    void editTrainingSession(Long id,
                             TrainingSessionDto trainingSessionDto);

    void deleteTrainingSession(Long id);

    TrainingSessionResponseDto getTrainingSessionDtoById(Long id);

    Optional<TrainingSession> getTrainingSessionById(Long id);

    boolean isAuthorized(TrainingSession trainingSession);

    List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser();
}
