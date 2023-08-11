package com.gymtracker.training_log;

import com.gymtracker.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/training-logs")
public class TrainingLogController {
    private final TrainingLogService trainingLogService;

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> editTrainingLog(@PathVariable Long id,
                                                           @RequestBody @Validated TrainingLogDto trainingLogDto) {
        trainingLogService.editTrainingLog(id, trainingLogDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training log id: " + id + " has been changed", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/gym-diary/{trainingSessionId}")
    public ResponseEntity<List<TrainingLogResponseDto>> getTrainingLogsForTrainingSession(@PathVariable("trainingSessionId") Long trainingSessionId) {
        List<TrainingLogResponseDto> trainingLogsForTrainingSession = trainingLogService.getTrainingLogsForTrainingSession(trainingSessionId);
        return new ResponseEntity<>(trainingLogsForTrainingSession, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteTrainingLogById(@PathVariable Long id) {
        trainingLogService.deleteTrainingLog(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training log id: " + id + " has been deleted", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
