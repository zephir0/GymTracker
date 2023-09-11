package com.gymtracker.training_session.service;

import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionRepository;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingSessionRetriever {
    private final TrainingSessionRepository trainingSessionRepository;
    private final UserService userService;

    public Optional<TrainingSession> getTrainingSessionById(Long id) {
        return trainingSessionRepository.findById(id);
    }

    public boolean isAuthorized(TrainingSession trainingSession) {
        return userService.getLoggedUser().getUserRole().equals(UserRoles.ADMIN) || trainingSession.getUser().getId().equals(userService.getLoggedUser().getId());
    }
}
