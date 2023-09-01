package com.gymtracker.training_session;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionService {
    void createTrainingSession(TrainingSessionDto trainingSessionDto);


    void deleteTrainingSession(Long id);

    TrainingSessionResponseDto getTrainingSessionDtoById(Long id);

    Optional<TrainingSession> getTrainingSessionById(Long id);


    List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser();

}
