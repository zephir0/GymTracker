package com.gymtracker.training_session;

import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingSessionServiceImpl implements TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final UserService userService;

    @Override
    public void createTrainingSession(TrainingSessionDto trainingSessionDto) {

        TrainingSession trainingSession = trainingSessionMapper.toEntity(trainingSessionDto, userService.getLoggedUser());
        trainingSessionRepository.save(trainingSession);
    }

    @Override
    public void editTrainingSession(Long id,
                                    TrainingSessionDto trainingSessionDto) {
        trainingSessionRepository.findById(id).map(this::isTrainingSessionCreatorOrAdmin).ifPresentOrElse(trainingSession -> {
            trainingSession.setTrainingName(trainingSessionDto.trainingName());
            trainingSessionRepository.save(trainingSession);
        }, () -> {
            throw new TrainingSessionNotFoundException("Training Session doesn't exist in database");
        });
    }

    @Override
    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.findById(id).map(this::isTrainingSessionCreatorOrAdmin).ifPresentOrElse(trainingSession -> {
            trainingSessionRepository.deleteById(id);
        }, () -> {
            throw new TrainingSessionNotFoundException("Training session doesn't exist in database.");
        });
    }

    @Override
    public TrainingSessionResponseDto getTrainingSessionDtoById(Long id) {
        return trainingSessionRepository.findById(id)
                .map(this::isTrainingSessionCreatorOrAdmin)
                .map(trainingSessionMapper::toDto)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));
    }

    @Override
    public Optional<TrainingSession> getTrainingSessionById(Long id) {
        return trainingSessionRepository.findById(id);
    }

    @Override
    public boolean isAuthorized(TrainingSession trainingSession) {
        return userService.getLoggedUser().getUserRole().equals(UserRoles.ADMIN) || trainingSession.getUser().getId().equals(userService.getLoggedUser().getId());
    }


    @Override
    public List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();
        return trainingSessionRepository.findAllByUser(loggedUser).stream().map(trainingSessionMapper::toDto
        ).collect(Collectors.toList());
    }

    private TrainingSession isTrainingSessionCreatorOrAdmin(TrainingSession trainingSession) {
        if (isAuthorized(trainingSession)) {
            return trainingSession;
        } else
            throw new UnauthorizedTrainingSessionAccessException("You are not authorized to access that training session.");
    }


}
