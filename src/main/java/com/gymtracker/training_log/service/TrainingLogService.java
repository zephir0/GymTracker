package com.gymtracker.training_log.service;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_session.TrainingSession;

import java.util.List;

public interface TrainingLogService {
    void createTrainingLogs(List<TrainingLogDto> trainingLogDtoList,
                            Long createdSessionId);

    void deleteTrainingLog(Long trainingLogId);

    void editTrainingLog(Long trainingLogId,
                         TrainingLogDto trainingLogDto);

    List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long trainingSessionId);

    List<TrainingLog> getAllByExerciseId(Long exerciseId);

    List<TrainingLog> getAllByTrainingSessionId(Long trainingSessionId);

    List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                             Long trainingSessionId);

    boolean checkAuthorization(TrainingSession trainingSession);
}
