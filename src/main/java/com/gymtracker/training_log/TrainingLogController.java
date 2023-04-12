package com.gymtracker.training_log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/training-logs")
public class TrainingLogController {
    private final TrainingLogService trainingLogService;

    @PostMapping
    public ResponseEntity<String> createTrainingLog(@RequestBody @Valid TrainingLogDto trainingLogDto) {
        trainingLogService.createTrainingLog(trainingLogDto);
        return new ResponseEntity<>("Training log has been created successfully", HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editTrainingLog(@PathVariable Long id,
                                                  @RequestBody TrainingLogDto trainingLogDto) {
        trainingLogService.editTrainingLog(id, trainingLogDto);
        return new ResponseEntity<>("Training log id: " + id + " has been changed", HttpStatus.OK);
    }

    @GetMapping("/gym-diary/{id}")
    public ResponseEntity<List<TrainingLogResponseDto>> getTrainingLogsForTrainingSession(@PathVariable Long id) {
        List<TrainingLogResponseDto> trainingLogsForTrainingSession = trainingLogService.getTrainingLogsForTrainingSession(id);
        return new ResponseEntity<>(trainingLogsForTrainingSession, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainingLogById(@PathVariable Long id) {
        trainingLogService.deleteTrainingLog(id);
        return new ResponseEntity<>("Training log id: " + id + " has been deleted", HttpStatus.OK);
    }

}
