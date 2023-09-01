package com.gymtracker.training_session;

import com.gymtracker.training_log.TrainingLogService;
import com.gymtracker.training_routine.UnauthorizedAccessTrainingRoutineException;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingSessionServiceImpl implements TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final UserService userService;
    private final TrainingLogService trainingLogService;

    @Override
    @Transactional
    public void createTrainingSession(TrainingSessionDto trainingSessionDto) {
        TrainingSession trainingSession = trainingSessionMapper.toEntity(trainingSessionDto, userService.getLoggedUser());

        if (trainingSession.getUser().getId().equals(trainingSession.getTrainingRoutine().getUser().getId())) {
            Long createdSessionId = trainingSessionRepository.save(trainingSession).getId();
            trainingLogService.createTrainingLogs(trainingSessionDto, createdSessionId);
        } else
            throw new UnauthorizedAccessTrainingRoutineException("You are not authorized to use that training routine");
    }


    @Override
    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.findById(id).map(this::verifyUserAuthorizedForTrainingSession).ifPresentOrElse(trainingSession -> {
            trainingSessionRepository.deleteById(id);
        }, () -> {
            throw new TrainingSessionNotFoundException("Training session doesn't exist in database.");
        });
    }

    @Override
    public TrainingSessionResponseDto getTrainingSessionDtoById(Long id) {
        return trainingSessionRepository.findById(id)
                .map(this::verifyUserAuthorizedForTrainingSession)
                .map(trainingSessionMapper::toDto)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found")
                );
    }

    @Override
    public Optional<TrainingSession> getTrainingSessionById(Long id) {
        return trainingSessionRepository.findById(id);
    }


    @Override
    public List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();

        List<TrainingSession> trainingSessions = trainingSessionRepository.findTrainingSessionsByUser(loggedUser);

        List<Object[]> sessionAndTotalWeights = trainingSessionRepository.findTotalWeightsForTrainingSessions(trainingSessions);

        return sessionAndTotalWeights.stream().map(data -> {
            TrainingSession trainingSession = (TrainingSession) data[0];
            Long totalWeight = (Long) data[1];
            return trainingSessionMapper.toDto(trainingSession, totalWeight);
        }).collect(Collectors.toList());
    }

    private boolean isUserAuthorizedForTrainingSession(TrainingSession trainingSession) {
        return userService.getLoggedUser().getUserRole().equals(UserRoles.ADMIN) || trainingSession.getUser().getId().equals(userService.getLoggedUser().getId());
    }


    private TrainingSession verifyUserAuthorizedForTrainingSession(TrainingSession trainingSession) {
        if (isUserAuthorizedForTrainingSession(trainingSession)) {
            return trainingSession;
        } else
            throw new UnauthorizedTrainingSessionAccessException("You are not authorized to access that training session.");
    }


}
