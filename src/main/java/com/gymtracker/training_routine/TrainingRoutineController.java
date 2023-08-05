package com.gymtracker.training_routine;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_log.TrainingLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    ResponseEntity<SuccessResponse> deleteTrainingRoutine(@PathVariable("id") Long id) {
        trainingRoutineService.archiveTrainingRoutine(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training routine has been archived.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/previous-logs/{routineId}")
    ResponseEntity<Map<Long, TrainingLogResponseDto>> getPreviousTrainingLogsForTrainingRoutine(@PathVariable("routineId") Long routineId) {
        Map<Long, TrainingLogResponseDto> previousTrainingEntries = trainingRoutineService.getPreviousTrainingEntries(routineId);
        return new ResponseEntity<>(previousTrainingEntries, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    ResponseEntity<TrainingRoutine> getTrainingRoutine(@PathVariable Long id) {
        TrainingRoutine trainingRoutine = trainingRoutineService.getTrainingRoutine(id);
        return new ResponseEntity<>(trainingRoutine, HttpStatus.OK);
    }

    @GetMapping("/user")
    ResponseEntity<List<TrainingRoutine>> getTrainingRoutinesForUser() {
        List<TrainingRoutine> allTrainingRoutinesForUser = trainingRoutineService.getTrainingRoutines(false);
        return new ResponseEntity<>(allTrainingRoutinesForUser, HttpStatus.OK);
    }

    @GetMapping("/user/archived")
    ResponseEntity<List<TrainingRoutine>> getArchivedTrainingRoutinesForUser() {
        List<TrainingRoutine> allTrainingRoutinesForUser = trainingRoutineService.getTrainingRoutines(true);
        return new ResponseEntity<>(allTrainingRoutinesForUser, HttpStatus.OK);
    }
}
