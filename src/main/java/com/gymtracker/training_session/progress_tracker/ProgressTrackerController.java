package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.training_log.TrainingLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/progress-tracker/")
public class ProgressTrackerController {
    private final ProgressTrackerService progressTrackerService;

    @GetMapping("/exercises/{exerciseDescription}/sort-by-max-weight-and-max-rep")
    public ResponseEntity<SuccessResponse> sortByMaxWeightAndMaxRepForExercise(@PathVariable String exerciseDescription) {
        List<TrainingLog> sortedByMaxWeightForExercise = progressTrackerService.sortByMaxWeightAndMaxRepForExercise(exerciseDescription);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, sortedByMaxWeightForExercise, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/training-sessions/{trainingSessionId}/total-weight")
    public ResponseEntity<SuccessResponse> getTotalWeightForTrainingSession(@PathVariable Long trainingSessionId) {
        Integer totalWeight = progressTrackerService.calculateWeightForTrainingSession(trainingSessionId);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, totalWeight, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/training-sessions/{trainingSessionId}/exercises/{exerciseDescription}/total-weight")
    public ResponseEntity<SuccessResponse> getTotalWeightForExerciseFromTrainingSession(@PathVariable Long trainingSessionId,
                                                                                        @PathVariable String exerciseDescription) {
        Integer totalWeight = progressTrackerService.calculateTotalWeightForExerciseFromTrainingSession(trainingSessionId, exerciseDescription);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, totalWeight, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
