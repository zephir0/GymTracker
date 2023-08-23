package com.gymtracker.training_routine;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.training_log.TrainingLogResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Training Routine API")
public class TrainingRoutineController {
    private final TrainingRoutineService trainingRoutineService;

    @PostMapping()
    @ApiOperation("Create a new training routine")
    ResponseEntity<SuccessResponse> createTrainingRoutine(@RequestBody @Validated TrainingRoutineDto trainingRoutineDto) {
        trainingRoutineService.createTrainingRoutine(trainingRoutineDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "New training routine has been created.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Archive a training routine")
    ResponseEntity<SuccessResponse> deleteTrainingRoutine(@PathVariable("id") Long id) {
        trainingRoutineService.archiveTrainingRoutine(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training routine has been archived.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/previous-logs/{routineId}")
    @ApiOperation("Get previous training logs for a training routine")
    ResponseEntity<Map<Long, TrainingLogResponseDto>> getPreviousTrainingLogsForTrainingRoutine(@PathVariable("routineId") Long routineId) {
        Map<Long, TrainingLogResponseDto> previousTrainingEntries = trainingRoutineService.getPreviousTrainingEntries(routineId);
        return new ResponseEntity<>(previousTrainingEntries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get details of a training routine")
    ResponseEntity<TrainingRoutine> getTrainingRoutine(@PathVariable Long id) {
        TrainingRoutine trainingRoutine = trainingRoutineService.getTrainingRoutine(id);
        return new ResponseEntity<>(trainingRoutine, HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiOperation("Get all training routines for a user")
    ResponseEntity<List<TrainingRoutine>> getTrainingRoutinesForUser() {
        List<TrainingRoutine> allTrainingRoutinesForUser = trainingRoutineService.getTrainingRoutines(false);
        return new ResponseEntity<>(allTrainingRoutinesForUser, HttpStatus.OK);
    }

    @GetMapping("/user/archived")
    @ApiOperation("Get archived training routines for a user")
    ResponseEntity<List<TrainingRoutine>> getArchivedTrainingRoutinesForUser() {
        List<TrainingRoutine> allTrainingRoutinesForUser = trainingRoutineService.getTrainingRoutines(true);
        return new ResponseEntity<>(allTrainingRoutinesForUser, HttpStatus.OK);
    }
}
