package com.gymtracker.training_log;

import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionDto;
import com.gymtracker.training_session.TrainingSessionRetriever;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingLogServiceImpl implements TrainingLogService {

    private final TrainingLogRepository trainingLogRepository;
    private final TrainingLogMapper trainingLogMapper;
    private final TrainingSessionRetriever trainingSessionRetriever;
    private final ExerciseService exerciseService;

    @Override
    public void createTrainingLogs(TrainingSessionDto trainingSessionDto, Long createdSessionId) {
        trainingSessionDto.trainingLogDtoList().stream().map((trainingLog) -> trainingLogMapper.toEntity(trainingLog, createdSessionId)).forEach(trainingLogRepository::save);
    }

    @Override
    public void deleteTrainingLog(Long id) {
        trainingLogRepository.findById(id).filter(trainingLog -> checkAuthorization(trainingLog.getTrainingSession())).ifPresentOrElse(trainingLog -> {
            trainingLogRepository.deleteById(trainingLog.getId());
        }, () -> {
            throw new TrainingLogNotFoundException("Training log doesn't exist");
        });
    }

    @Override
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


    @Override
    public List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(Long id) {
        TrainingSession trainingSession = trainingSessionRetriever.getTrainingSessionById(id).filter(this::checkAuthorization).orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));

        List<TrainingLogResponseDto> trainingLogResponseDtoList = trainingSession.getTrainingLogs().stream().map(map -> {
            String name = map.getExercise().getName();
            return trainingLogMapper.toDto(map, name);
        }).collect(Collectors.toList());

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
        TrainingSession trainingSession = trainingSessionRetriever.getTrainingSessionById(trainingSessionId).filter(this::checkAuthorization).orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));

        return trainingSession.getTrainingLogs();
    }

    @Override
    public List<TrainingLog> getAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                                    Long trainingSessionId) {
        TrainingSession trainingSession = trainingSessionRetriever
                .getTrainingSessionById(trainingSessionId)
                .filter(this::checkAuthorization)
                .orElseThrow(() -> new TrainingSessionNotFoundException("Training session not found"));

        exerciseService.existById(exerciseId);

        return trainingSession.getTrainingLogs()
                .stream()
                .filter(trainingLog ->
                        trainingLog.getExercise().getId().equals(exerciseId))
                .collect(Collectors.toList());
    }


    private boolean checkAuthorization(TrainingSession trainingSession) {
        if (!trainingSessionRetriever.isAuthorized(trainingSession)) {
            throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin");
        } else return true;
    }
}
