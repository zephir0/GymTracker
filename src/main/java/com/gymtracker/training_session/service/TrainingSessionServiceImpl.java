package com.gymtracker.training_session.service;

import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_routine.TrainingRoutineService;
import com.gymtracker.training_routine.exception.UnauthorizedTrainingRoutineAccessException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionRepository;
import com.gymtracker.training_session.dto.TrainingSessionDto;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.training_session.mapper.TrainingSessionMapper;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import com.gymtracker.user.service.UserService;
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
    private final TrainingRoutineService trainingRoutineService;

    @Override
    @Transactional
    public void createTrainingSession(TrainingSessionDto trainingSessionDto) {

        TrainingRoutine trainingRoutine = trainingRoutineService.getTrainingRoutine(trainingSessionDto.trainingRoutineId());

        TrainingSession trainingSession = trainingSessionMapper.toEntity(trainingRoutine, userService.getLoggedUser());

        if (trainingSession.getUser().getId().equals(trainingSession.getTrainingRoutine().getUser().getId())) {
            Long createdSessionId = trainingSessionRepository.save(trainingSession).getId();
            trainingLogService.createTrainingLogs(trainingSessionDto.trainingLogDtoList(), createdSessionId);
        } else
            throw new UnauthorizedTrainingRoutineAccessException("You are not authorized to use that training routine");
    }


    @Override
    public void deleteTrainingSession(Long trainingSessionId) {
        trainingSessionRepository.findById(trainingSessionId)
                .map(this::verifyUserAuthorizedForTrainingSession)
                .ifPresentOrElse(trainingSession -> {
                    trainingSessionRepository.deleteById(trainingSessionId);
                }, () -> {
                    throw new TrainingSessionNotFoundException("Training session doesn't exist.");
                });
    }

    @Override
    public TrainingSessionResponseDto getTrainingSessionDtoById(Long trainingSessionId) {
        return trainingSessionRepository.findById(trainingSessionId)
                .map(this::verifyUserAuthorizedForTrainingSession)
                .map(trainingSessionMapper::toDto)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found")
                );
    }

    @Override
    public Optional<TrainingSession> getTrainingSessionById(Long trainingSessionId) {
        return trainingSessionRepository.findById(trainingSessionId);
    }


    @Override
    public List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();

        List<TrainingSession> trainingSessions = trainingSessionRepository.findTrainingSessionsByUser(loggedUser);
        if (trainingSessions.isEmpty()) {
            throw new TrainingSessionNotFoundException("Training sessions were not found for that user.");
        }

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
