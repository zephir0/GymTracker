package com.gymtracker.gym_diary.progress_tracker;

import com.gymtracker.diary_log.DiaryLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/progress-tracker/")
public class ProgressTrackerController {
    private final ProgressTrackerService progressTrackerService;

    @GetMapping("/{exerciseDescription}/sort-by-max-weight-and-max-rep")
    public ResponseEntity<List<DiaryLog>> sortByMaxWeightAndMaxRepForExercise(@PathVariable String exerciseDescription) {
        List<DiaryLog> sortedByMaxWeightForExercise = progressTrackerService.sortByMaxWeightAndMaxRepForExercise(exerciseDescription);
        return new ResponseEntity<>(sortedByMaxWeightForExercise, HttpStatus.OK);
    }

    @GetMapping("/{gymDiaryId}/total-weight")
    public ResponseEntity<Integer> getTotalWeightForTrainingSession(@PathVariable Long gymDiaryId) {
        Integer totalWeight = progressTrackerService.calculateWeightForTrainingSession(gymDiaryId);
        return new ResponseEntity<>(totalWeight, HttpStatus.OK);
    }

    @GetMapping("/{gymDiaryId}/{exerciseDescription}/total-weight")
    public ResponseEntity<Integer> getTotalWeightForExerciseFromTrainingSession(@PathVariable Long gymDiaryId,
                                                                                @PathVariable String exerciseDescription) {
        Integer totalWeight = progressTrackerService.calculateTotalWeightForExerciseFromTrainingSession(gymDiaryId, exerciseDescription);
        return new ResponseEntity<>(totalWeight, HttpStatus.OK);
    }
}
