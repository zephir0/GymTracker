package com.gymtracker.training_log;

import com.gymtracker.training_log.dto.TrainingLogDto;
import com.gymtracker.training_log.dto.TrainingLogResponseDto;
import com.gymtracker.training_log.service.TrainingLogService;
import response_model.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/training-logs")
@Api(tags = "Training Log API")
public class TrainingLogController {
    private final TrainingLogService trainingLogService;

    @PutMapping("/{trainingLogId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit a training log")
    public SuccessResponse editTrainingLog(@PathVariable Long trainingLogId,
                                           @RequestBody @Validated TrainingLogDto trainingLogDto) {
        trainingLogService.editTrainingLog(trainingLogId, trainingLogDto);
        return new SuccessResponse(HttpStatus.OK, "Training log id: " + trainingLogId + " has been changed", LocalDateTime.now());
    }

    @GetMapping("/session/{trainingSessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get training logs for a training session")
    public List<TrainingLogResponseDto> getTrainingLogsForTrainingSession(@PathVariable("trainingSessionId") Long trainingSessionId) {
        return trainingLogService.getTrainingLogsForTrainingSession(trainingSessionId);
    }

    @DeleteMapping("/{trainingLogId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a training log")
    public SuccessResponse deleteTrainingLogById(@PathVariable Long trainingLogId) {
        trainingLogService.deleteTrainingLog(trainingLogId);
        return new SuccessResponse(HttpStatus.OK, "Training log id: " + trainingLogId + " has been deleted", LocalDateTime.now());
    }
}
