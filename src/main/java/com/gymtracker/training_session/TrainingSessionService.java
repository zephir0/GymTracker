package com.gymtracker.training_session;

import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final UserService userService;

    public void createTrainingSession(TrainingSessionDto trainingSessionDto) {

        TrainingSession trainingSession = trainingSessionMapper.toEntity(trainingSessionDto, userService.getLoggedUser());
        trainingSession.setTrainingDate(LocalDateTime.now());
        trainingSessionRepository.save(trainingSession);
    }

    public void editTrainingSession(Long id,
                                    TrainingSessionDto trainingSessionDto) {
        trainingSessionRepository.findById(id).ifPresentOrElse(trainingSession -> {
            if (isTrainingSessionCreatorOrAdmin(trainingSession, userService.getLoggedUser())) {
                trainingSession.setTrainingName(trainingSessionDto.trainingName());
                trainingSessionRepository.save(trainingSession);
            } else throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin.");
        }, () -> {
            throw new TrainingSessionNotFoundException("Training Session doesn't exist in database");
        });
    }

    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.findById(id).ifPresentOrElse(trainingSession -> {
            if (isTrainingSessionCreatorOrAdmin(trainingSession, userService.getLoggedUser())) {
                trainingSessionRepository.deleteById(id);
            } else throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin.");
        }, () -> {
            throw new TrainingSessionNotFoundException("Training session doesn't exist in database");
        });
    }

    public TrainingSession findById(Long id) {
        return trainingSessionRepository.findById(id)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session was not found."));
    }


    public boolean isTrainingSessionCreatorOrAdmin(TrainingSession trainingSession,
                                                   User user) {
        return (user.getUserRole().equals(UserRoles.ADMIN) || trainingSession.getUser().getId().equals(user.getId()));
    }


    public List<TrainingSession> getAllTrainingSessionsForLoggedUser() {
        return trainingSessionRepository.findAllByUser(userService.getLoggedUser());
    }


}
