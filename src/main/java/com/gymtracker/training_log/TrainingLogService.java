package com.gymtracker.training_log;

import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionService;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingLogService {

    private final TrainingLogRepository trainingLogRepository;
    private final TrainingLogMapper trainingLogMapper;
    private final TrainingSessionService trainingSessionService;
    private final ExerciseService exerciseService;

    public void createTrainingLog(TrainingLogDto trainingLogDto) {

        if (exerciseService.existById(trainingLogDto.trainingSessionId())) {
            throw new ExerciseNotFoundException("Exercise with that id doesn't exist");
        }

        trainingSessionService.getById(trainingLogDto.trainingSessionId()).filter(this::checkAuthorization).ifPresentOrElse(session -> {
            TrainingLog trainingLog = trainingLogMapper.toEntity(trainingLogDto);
            trainingLogRepository.save(trainingLog);
        }, () -> {
            throw new TrainingSessionNotFoundException("Training session not found");
        });
    }

    public void deleteTrainingLog(Long id) {
        trainingLogRepository.findById(id)
                .filter(trainingLog -> checkAuthorization(trainingLog.getTrainingSession()))
                .ifPresentOrElse(trainingLog -> {
                    trainingLogRepository.deleteById(trainingLog.getId());
                }, () -> {
                    throw new TrainingLogNotFoundException("Training log doesn't exist");
                });
    }

    public void editTrainingLog(Long id,
                                TrainingLogDto trainingLogDto) {
        trainingLogRepository.findById(id).filter(trainingLog -> checkAuthorization(trainingLog.getTrainingSession())).ifPresentOrElse(trainingLog -> {
            trainingLog.setReps(trainingLogDto.reps());
            trainingLog.setWeight(trainingLogDto.weight());
            trainingLog.setPersonalNotes(trainingLogDto.personalNotes());
            trainingLogRepository.save(trainingLog);
        }, () -> {
            throw new TrainingLogNotFoundException("Training log doesn't exist");
        });
    }


    public List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long id) {
        TrainingSession trainingSession = trainingSessionService.getById(id)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));

        List<TrainingLogResponseDto> trainingLogResponseDtoList = trainingSession.getTrainingLogs().stream().map(trainingLogMapper::toDto).collect(Collectors.toList());

        if (trainingLogResponseDtoList.isEmpty()) {
            throw new TrainingLogNotFoundException("There is no logs in that training session");
        } else return trainingLogResponseDtoList;

    }

    public List<TrainingLog> getAllByExerciseId(Long exerciseId) {
        return trainingLogRepository.findAllByExerciseId(exerciseId);
    }

    public List<TrainingLog> getAllByTrainingSessionId(Long trainingSessionId) {
        TrainingSession trainingSession = trainingSessionService.getById(trainingSessionId)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));

        return trainingSession.getTrainingLogs();
    }

    public List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                                    Long trainingSessionId) {
        TrainingSession trainingSession = trainingSessionService.getById(trainingSessionId)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));
        exerciseService.existById(exerciseId);
        return trainingSession.getTrainingLogs().stream().filter(trainingLog -> trainingLog.getExercise().getId().equals(exerciseId)).collect(Collectors.toList());
    }


    private boolean checkAuthorization(TrainingSession trainingSession) {
        if (!trainingSessionService.isAuthorized(trainingSession)) {
            throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin");
        } else return true;
    }
}
