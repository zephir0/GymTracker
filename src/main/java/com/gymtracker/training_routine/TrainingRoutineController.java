package com.gymtracker.training_routine;

import com.gymtracker.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/training-routines")
public class TrainingRoutineController {
    private final TrainingRoutineService trainingRoutineService;

    @PostMapping()
    ResponseEntity<SuccessResponse> createTrainingRoutine(@RequestBody @Validated TrainingRoutineDto trainingRoutineDto) {
        trainingRoutineService.createTrainingRoutine(trainingRoutineDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "New training routine has been created.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<TrainingRoutine> getTrainingRoutine(@PathVariable Long id) {
        TrainingRoutine trainingRoutine = trainingRoutineService.getTrainingRoutine(id);
        return new ResponseEntity<>(trainingRoutine, HttpStatus.OK);
    }

    @GetMapping("/user")
    ResponseEntity<List<TrainingRoutine>> getAllTrainingRoutinesForUser() {
        List<TrainingRoutine> allTrainingRoutinesForUser = trainingRoutineService.getAllTrainingRoutinesForUser();
        return new ResponseEntity<>(allTrainingRoutinesForUser, HttpStatus.OK);
    }
}
