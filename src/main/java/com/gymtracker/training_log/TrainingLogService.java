package com.gymtracker.training_log;

import java.util.List;

public interface TrainingLogService {
    void createTrainingLog(TrainingLogDto trainingLogDto);

    void deleteTrainingLog(Long id);

    void editTrainingLog(Long id,
                         TrainingLogDto trainingLogDto);

    List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long id);

    List<TrainingLog> getAllByExerciseId(Long exerciseId);

    List<TrainingLog> getAllByTrainingSessionId(Long trainingSessionId);

    List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                             Long trainingSessionId);
}
