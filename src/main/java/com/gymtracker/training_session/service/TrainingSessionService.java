package com.gymtracker.training_session.service;

import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.dto.TrainingSessionDto;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionService {
    void createTrainingSession(TrainingSessionDto trainingSessionDto);

    void deleteTrainingSession(Long trainingSessionId);

    TrainingSessionResponseDto getTrainingSessionDtoById(Long trainingSessionId);

    Optional<TrainingSession> getTrainingSessionById(Long trainingSessionId);


    List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser();

}
