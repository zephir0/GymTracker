package com.gymtracker.training_log;

import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionDto;

import java.util.List;

public interface TrainingLogService {
    void createTrainingLogs(TrainingSessionDto trainingSessionDto,
                            Long createdSessionId);

    void deleteTrainingLog(Long id);

    void editTrainingLog(Long id,
                         TrainingLogDto trainingLogDto);

    List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long id);

    List<TrainingLog> getAllByExerciseId(Long exerciseId);

    List<TrainingLog> getAllByTrainingSessionId(Long trainingSessionId);

    List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                             Long trainingSessionId);

    boolean checkAuthorization(TrainingSession trainingSession);
}
