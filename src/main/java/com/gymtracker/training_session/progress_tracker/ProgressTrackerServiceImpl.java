package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogMapper;
import com.gymtracker.training_log.TrainingLogResponseDto;
import com.gymtracker.training_log.TrainingLogService;
import com.gymtracker.training_session.TrainingSessionRepository;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressTrackerServiceImpl implements ProgressTrackerService {
    private final TrainingLogService trainingLogService;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingLogMapper trainingLogMapper;
    private final UserService userService;

    @Override
    public List<TrainingLogResponseDto> sortByMaxWeightAndRepsForExercise(Long exerciseId) {
        List<TrainingLog> trainingLogs = trainingLogService.getAllByExerciseId(exerciseId);
        return sortByWeightAndReps(trainingLogs).stream().map(trainingLogMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Long calculateTotalWeightForSession(Long trainingSessionId) {
        List<TrainingLog> trainingLogList = trainingLogService.getAllByTrainingSessionId(trainingSessionId);
        return calculateWeight(trainingLogList);
    }

    @Override
    public Long calculateExerciseWeightForSession(Long exerciseId,
                                                  Long trainingSessionId) {
        List<TrainingLog> trainingLogList = trainingLogService.getAllByExerciseIdAndTrainingSessionId(exerciseId, trainingSessionId);
        return calculateWeight(trainingLogList);
    }

    @Override
    public Long countTrainingSessions() {
        User loggedUser = userService.getLoggedUser();
        return trainingSessionRepository.countByUser(loggedUser);
    }

    private Long calculateWeight(List<TrainingLog> trainingLogs) {
        return (long) trainingLogs.stream().mapToInt(TrainingLog::getWeight).sum();
    }

    private List<TrainingLog> sortByWeightAndReps(List<TrainingLog> trainingLogs) {
        return trainingLogs.stream().sorted(Comparator.comparing(TrainingLog::getWeight, Comparator.reverseOrder()).thenComparing(TrainingLog::getReps, Comparator.reverseOrder())).collect(Collectors.toList());
    }


}
