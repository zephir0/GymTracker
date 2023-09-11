package com.gymtracker.training_log.service;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.TrainingLogRepository;
import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.exception.EmptyTrainingLogListException;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.service.TrainingSessionRetriever;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingLogServiceImpl implements TrainingLogService {

    private final TrainingLogRepository trainingLogRepository;
    private final TrainingLogMapper trainingLogMapper;
    private final TrainingSessionRetriever trainingSessionRetriever;

    @Override
    @Transactional
    public void createTrainingLogs(List<TrainingLogDto> trainingLogDtoList,
                                   Long createdSessionId) {

        if (trainingLogDtoList.isEmpty()) {
            throw new EmptyTrainingLogListException("There is no training logs in that list");
        } else {
            trainingLogDtoList.stream()
                    .map(
                            (trainingLogDto) -> trainingLogMapper.toEntity(trainingLogDto, createdSessionId)
                    )
                    .forEach(trainingLogRepository::save);
        }
    }

    @Override
    public void deleteTrainingLog(Long trainingLogId) {
        trainingLogRepository.findById(trainingLogId)
                .filter(trainingLog -> checkAuthorization(trainingLog.getTrainingSession()))
                .ifPresentOrElse(
                        trainingLog -> trainingLogRepository.deleteById(trainingLog.getId()),
                        () -> {
                            throw new TrainingLogNotFoundException("Training log doesn't exist");
                        }
                );
    }

    @Override
    public void editTrainingLog(Long trainingLogId,
                                TrainingLogDto trainingLogDto) {
        trainingLogRepository.findById(trainingLogId)
                .filter(trainingLog -> checkAuthorization(trainingLog.getTrainingSession()))
                .ifPresentOrElse(trainingLog -> {
                    trainingLog.setReps(trainingLogDto.reps());
                    trainingLog.setWeight(trainingLogDto.weight());
                    trainingLog.setPersonalNotes(trainingLogDto.personalNotes());
                    trainingLogRepository.save(trainingLog);
                }, () -> {
                    throw new TrainingLogNotFoundException("Training log doesn't exist");
                });
    }


    @Override
    public List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long trainingSessionId) {
        TrainingSession trainingSession = getAuthorizedTrainingSession(trainingSessionId);

        List<TrainingLogResponseDto> trainingLogResponseDtoList = trainingSession
                .getTrainingLogs()
                .stream()
                .map(map -> {
                    String name = map.getExercise().getName();
                    return trainingLogMapper.toDto(map, name);
                })
                .collect(Collectors.toList());

        if (trainingLogResponseDtoList.isEmpty()) {
            throw new TrainingLogNotFoundException("There is no logs in that training session");

        } else return trainingLogResponseDtoList;

    }

    @Override
    public List<TrainingLog> getAllByExerciseId(Long exerciseId) {
        return trainingLogRepository.findAllByExerciseId(exerciseId);
    }

    @Override
    public List<TrainingLog> getAllByTrainingSessionId(Long trainingSessionId) {
        TrainingSession trainingSession = getAuthorizedTrainingSession(trainingSessionId);

        return trainingSession.getTrainingLogs();
    }


    @Override
    public List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                                    Long trainingSessionId) {
        TrainingSession trainingSession = trainingSessionRetriever
                .getTrainingSessionById(trainingSessionId)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));


        return trainingSession.getTrainingLogs()
                .stream()
                .filter(trainingLog ->
                        trainingLog.getExercise().getId().equals(exerciseId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkAuthorization(TrainingSession trainingSession) {
        if (!trainingSessionRetriever.isAuthorized(trainingSession)) {
            throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin");
        } else return true;
    }

    private TrainingSession getAuthorizedTrainingSession(Long trainingSessionId) {
        return trainingSessionRetriever.getTrainingSessionById(trainingSessionId)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));
    }
}
