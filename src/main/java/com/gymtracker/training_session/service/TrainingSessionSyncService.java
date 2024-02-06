package com.gymtracker.training_session.service;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.service.TrainingLogService;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionRepository;
import com.gymtracker.training_session.dto.TrainingSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingSessionSyncService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionService trainingSessionService;
    private final TrainingLogMapper trainingLogMapper;
    private final TrainingLogService trainingLogService;

    @Transactional
    public void synchronizeTrainingSessions(List<TrainingSessionDto> trainingSessionDtoList) {
        List<TrainingSession> existingTrainingSessions = trainingSessionRepository.findAll();

        removeObsoleteTrainingSessions(existingTrainingSessions, trainingSessionDtoList);

        for (TrainingSessionDto trainingSessionDto : trainingSessionDtoList) {
            TrainingSession existingTrainingSession = findOrCreateTrainingSession(existingTrainingSessions, trainingSessionDto);

            trainingSessionService.verifyUserAuthorizedForTrainingSession(existingTrainingSession);


            trainingSessionRepository.save(existingTrainingSession);
            updateTrainingLogs(existingTrainingSession, trainingSessionDto.trainingLogDtoList());

        }
    }

    private void removeObsoleteTrainingSessions(List<TrainingSession> existingTrainingSessions,
                                                List<TrainingSessionDto> trainingSessionDtoList) {
        List<TrainingSession> obsoleteTrainingSessions = existingTrainingSessions.stream()
                .filter(existingSession -> trainingSessionDtoList.stream()
                        .noneMatch(dto -> dto.trainingRoutineId().equals(existingSession.getTrainingRoutine().getId())
                                && dto.trainingDate().toString().equals(existingSession.getTrainingDate().toString())))
                .collect(Collectors.toList());

        trainingSessionRepository.deleteAll(obsoleteTrainingSessions);
        existingTrainingSessions.removeAll(obsoleteTrainingSessions);
    }

    private TrainingSession findOrCreateTrainingSession(List<TrainingSession> existingTrainingSessions,
                                                        TrainingSessionDto trainingSessionDto) {
        return existingTrainingSessions.stream()
                .filter(session -> session.getTrainingRoutine().getId().equals(trainingSessionDto.trainingRoutineId())
                        && session.getTrainingDate().toString().equals(trainingSessionDto.trainingDate().toString()))
                .findFirst()
                .orElseGet(() -> trainingSessionService.createTrainingSession(trainingSessionDto));
    }


    private void updateTrainingLogs(TrainingSession trainingSession,
                                    List<TrainingLogDto> trainingLogDtoList) {
        for (TrainingLogDto trainingLogDto : trainingLogDtoList) {
            TrainingLog existingTrainingLog = trainingSession.getTrainingLogs().stream()
                    .filter(log -> logMatchesDto(log, trainingLogDto))
                    .findFirst()
                    .orElse(null);

            if (existingTrainingLog != null) {
                trainingLogService.editTrainingLog(existingTrainingLog.getId(), trainingLogDto);
            } else {
                TrainingLog newTrainingLog = trainingLogMapper.toEntity(trainingLogDto, trainingSession.getId());
                trainingSession.getTrainingLogs().add(newTrainingLog);
            }
        }
    }


    private boolean logMatchesDto(TrainingLog trainingLog,
                                  TrainingLogDto trainingLogDto) {
        return trainingLog.getReps().equals(trainingLogDto.reps()) && Objects.equals(trainingLog.getWeight(), trainingLogDto.weight()) && Objects.equals(trainingLog.getPersonalNotes(), trainingLogDto.personalNotes()) && trainingLog.getExercise().getId().equals(trainingLogDto.exerciseId());
    }
}
