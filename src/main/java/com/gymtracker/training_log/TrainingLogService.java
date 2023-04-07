package com.gymtracker.training_log;

import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import com.gymtracker.training_log.exception.TrainingLogNotFoundException;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.training_session.TrainingSessionService;
import com.gymtracker.training_session.exception.TrainingSessionNotFoundException;
import com.gymtracker.training_session.exception.UnauthorizedTrainingSessionAccessException;
import com.gymtracker.user.UserService;
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
    private final UserService userService;

    public void createTrainingLog(TrainingLogDto trainingLogDto) {

        if (exerciseService.existById(trainingLogDto.trainingSessionId())) {
            throw new ExerciseNotFoundException("Exercise with that id doesn't exist");
        }

        TrainingSession trainingSession = trainingSessionService.findById(trainingLogDto.trainingSessionId());

        checkAuthorization(trainingSession);

        TrainingLog trainingLog = trainingLogMapper.toEntity(trainingLogDto);
        trainingLogRepository.save(trainingLog);
    }

    public void deleteTrainingLog(Long id) {
        trainingLogRepository.findById(id).ifPresentOrElse(trainingLog -> {
            checkAuthorization(trainingLog.getTrainingSession());
            trainingLogRepository.deleteById(trainingLog.getId());
        }, () -> {
            throw new TrainingLogNotFoundException("Training log doesn't exist");
        });
    }

    public void editTrainingLog(Long id,
                                TrainingLogDto trainingLogDto) {
        trainingLogRepository.findById(id).ifPresentOrElse(trainingLog -> {
            checkAuthorization(trainingLog.getTrainingSession());
            trainingLog.setReps(trainingLogDto.reps());
            trainingLog.setWeight(trainingLogDto.weight());
            trainingLog.setPersonalNotes(trainingLogDto.personalNotes());
            trainingLogRepository.save(trainingLog);
        }, () -> {
            throw new TrainingLogNotFoundException("Training log doesn't exist");
        });
    }


    public List<TrainingLog> getTrainingLogsForTrainingSession(Long id) {
        TrainingSession trainingSession = trainingSessionService.findById(id);

        checkAuthorization(trainingSession);

        List<TrainingLog> trainingLogs = trainingSession.getTrainingLogs();

        if (trainingLogs.isEmpty()) {
            throw new TrainingLogNotFoundException("There is no logs in that training session");
        } else return trainingLogs;

    }

    public List<TrainingLog> findAllByExerciseId(Long exerciseId) {
        return filterAuthorizedTrainingLogs(trainingLogRepository.findAllByExerciseId(exerciseId));
    }

    public List<TrainingLog> findAllByTrainingSessionId(Long trainingSessionId) {
        return filterAuthorizedTrainingLogs(trainingLogRepository.findAllByTrainingSessionId(trainingSessionId));
    }

    public List<TrainingLog> findAllByExerciseIdAndTrainingSessionId(Long exerciseId,
                                                                     Long trainingSessionId) {
        return filterAuthorizedTrainingLogs(trainingLogRepository.findAllByExerciseIdAndTrainingSessionId(exerciseId, trainingSessionId));
    }

    private void checkAuthorization(TrainingSession trainingSession) {
        if (!trainingSessionService.isTrainingSessionCreatorOrAdmin(trainingSession, userService.getLoggedUser())) {
            throw new UnauthorizedTrainingSessionAccessException("You are not a training session creator or admin");
        }
    }

    private List<TrainingLog> filterAuthorizedTrainingLogs(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().filter(trainingLog -> {
            try {
                checkAuthorization(trainingLog.getTrainingSession());
                return true;
            } catch (UnauthorizedTrainingSessionAccessException e) {
                return false;
            }
        }).collect(Collectors.toList());
    }
}
