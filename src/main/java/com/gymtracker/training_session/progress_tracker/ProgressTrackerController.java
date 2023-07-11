package com.gymtracker.training_session.progress_tracker;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.training_log.TrainingLogResponseDto;
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

    @GetMapping("/exercises/{exerciseId}/sort-by-weight-and-rep")
    public ResponseEntity<SuccessResponse> sortByMaxWeightAndRepsForExercise(@PathVariable Long exerciseId) {
        List<TrainingLogResponseDto> sortedByMaxWeightForExercise = progressTrackerService.sortByMaxWeightAndRepsForExercise(exerciseId);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, sortedByMaxWeightForExercise, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/training-sessions/{trainingSessionId}/weight")
    public ResponseEntity<SuccessResponse> calculateTotalWeightForSession(@PathVariable Long trainingSessionId) {
        Long totalWeight = progressTrackerService.calculateTotalWeightForSession(trainingSessionId);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, totalWeight, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/training-sessions/workouts-count")
    public ResponseEntity<Long> countWorkouts() {
        Long workoutCount = progressTrackerService.countTrainingSessions();
        return new ResponseEntity<>(workoutCount, HttpStatus.OK);
    }


    @GetMapping("/training-sessions/{trainingSessionId}/exercises/{exerciseId}/weight")
    public ResponseEntity<SuccessResponse> calculateExerciseWeightForSession(@PathVariable Long exerciseId,
                                                                             @PathVariable Long trainingSessionId) {
        Long totalWeight = progressTrackerService.calculateExerciseWeightForSession(exerciseId, trainingSessionId);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, totalWeight, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
