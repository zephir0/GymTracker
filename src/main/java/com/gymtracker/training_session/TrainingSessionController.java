package com.gymtracker.training_session;

import com.gymtracker.response.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/training-sessions")
@Api(tags = "Training Session API")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @PostMapping
    @ApiOperation("Create a new training session")
    ResponseEntity<SuccessResponse> createTrainingSession(@RequestBody @Validated TrainingSessionDto trainingSessionDto) {
        trainingSessionService.createTrainingSession(trainingSessionDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Training session has been created", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a training session by ID")
    ResponseEntity<SuccessResponse> deleteTrainingSession(@PathVariable Long id) {
        trainingSessionService.deleteTrainingSession(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training session has been deleted", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiOperation("Get all training sessions for logged user")
    ResponseEntity<List<TrainingSessionResponseDto>> getAllTrainingSessionsForLoggedUser() {
        List<TrainingSessionResponseDto> trainingSessions = trainingSessionService.getAllTrainingSessionsForLoggedUser();
        return new ResponseEntity<>(trainingSessions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a training session by ID")
    ResponseEntity<TrainingSessionResponseDto> getTrainingSessionById(@PathVariable Long id) {
        TrainingSessionResponseDto trainingSessionById = trainingSessionService.getTrainingSessionDtoById(id);
        return new ResponseEntity<>(trainingSessionById, HttpStatus.OK);
    }
}
