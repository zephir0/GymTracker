package com.gymtracker.gym_diary.progress_tracker;

import com.gymtracker.diary_log.DiaryLog;
import com.gymtracker.diary_log.DiaryLogService;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.exercise.ExerciseService;
import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressTrackerService {
    private final DiaryLogService diaryLogService;
    private final ExerciseService exerciseService;

    public List<DiaryLog> sortByMaxWeightAndMaxRepForExercise(String exerciseDescription) {
        Exercise referenceByDescription = getExerciseByReference(exerciseDescription);
        List<DiaryLog> allByGymDiaryId = diaryLogService.findAllByExerciseId(referenceByDescription.getId());
        return sortByWeightAndReps(allByGymDiaryId);
    }

    public Integer calculateWeightForTrainingSession(Long gymDiaryId) {
        List<DiaryLog> diaryLogList = diaryLogService.findAllByGymDiaryId(gymDiaryId);
        return calculateWeight(diaryLogList);
    }

    public Integer calculateTotalWeightForExerciseFromTrainingSession(Long gymDiaryId,
                                                                      String exerciseDescription) {
        Exercise exercise = getExerciseByReference(exerciseDescription);
        List<DiaryLog> diaryLogList = diaryLogService.findAllByExerciseIdAndGymDiaryId(gymDiaryId, exercise.getId());
        return calculateWeight(diaryLogList);
    }

    private Integer calculateWeight(List<DiaryLog> diaryLogs) {
        return diaryLogs.stream().mapToInt(DiaryLog::getWeight).sum();
    }

    private List<DiaryLog> sortByWeightAndReps(List<DiaryLog> diaryLogs) {
        return diaryLogs.stream().sorted(Comparator.comparing(DiaryLog::getWeight, Comparator.reverseOrder()).thenComparing(DiaryLog::getReps, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    private Exercise getExerciseByReference(String exerciseDescription) {
        return exerciseService.getReferenceByDescription(exerciseDescription).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found."));
    }

}
