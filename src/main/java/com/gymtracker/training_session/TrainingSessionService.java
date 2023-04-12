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
import java.util.stream.Collectors;

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
            } else
                throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin.");
        }, () -> {
            throw new TrainingSessionNotFoundException("Training Session doesn't exist in database");
        });
    }

    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.findById(id).ifPresentOrElse(trainingSession -> {
            if (isTrainingSessionCreatorOrAdmin(trainingSession, userService.getLoggedUser())) {
                trainingSessionRepository.deleteById(id);
            } else
                throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin.");
        }, () -> {
            throw new TrainingSessionNotFoundException("Training session doesn't exist in database.");
        });
    }

    public TrainingSessionResponseDto getTrainingSessionById(Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).map(session -> {
            if (isTrainingSessionCreatorOrAdmin(session, userService.getLoggedUser())) {
                return session;
            } else
                throw new UnauthorizedTrainingSessionAccessException("You are not authorized to access that training session.");
        }).orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));
        return trainingSessionMapper.toDto(trainingSession);
    }

    public TrainingSession findById(Long id) {
        return trainingSessionRepository.findById(id)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found."));
    }


    public boolean isTrainingSessionCreatorOrAdmin(TrainingSession trainingSession,
                                                   User user) {
        return (user.getUserRole().equals(UserRoles.ADMIN) || trainingSession.getUser().getId().equals(user.getId()));
    }


    public List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();
        return trainingSessionRepository.findAllByUser(loggedUser).stream().map(trainingSessionMapper::toDto
        ).collect(Collectors.toList());
    }


}
