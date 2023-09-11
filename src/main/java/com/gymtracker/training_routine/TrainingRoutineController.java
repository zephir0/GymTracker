package com.gymtracker.training_routine;

import response_model.SuccessResponse;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a new training routine")
    public SuccessResponse createTrainingRoutine(@RequestBody @Validated TrainingRoutineDto trainingRoutineDto) {
        trainingRoutineService.createTrainingRoutine(trainingRoutineDto);
        return new SuccessResponse(HttpStatus.CREATED, "New training routine has been created.", LocalDateTime.now());
    }

    @PutMapping("/{trainingRoutineId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Archive a training routine")
    public SuccessResponse deleteTrainingRoutine(@PathVariable Long trainingRoutineId) {
        trainingRoutineService.archiveTrainingRoutine(trainingRoutineId);
        return new SuccessResponse(HttpStatus.OK, "Training routine has been archived.", LocalDateTime.now());
    }

    @GetMapping("/previous-logs/{trainingRoutineId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get previous training logs for a training routine")
    public Map<Long, TrainingLogResponseDto> getPreviousTrainingLogsForTrainingRoutine(@PathVariable Long trainingRoutineId) {
        return trainingRoutineService.getPreviousTrainingEntries(trainingRoutineId);
    }

    @GetMapping("/{trainingRoutineId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get details of a training routine")
    public TrainingRoutine getTrainingRoutine(@PathVariable Long trainingRoutineId) {
        return trainingRoutineService.getTrainingRoutine(trainingRoutineId);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get all training routines for a user")
    public List<TrainingRoutine> getTrainingRoutinesForUser() {
        return trainingRoutineService.getTrainingRoutines(false);
    }

    @GetMapping("/user/archived")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get archived training routines for a user")
    public List<TrainingRoutine> getArchivedTrainingRoutinesForUser() {
        return trainingRoutineService.getTrainingRoutines(true);
    }
}
