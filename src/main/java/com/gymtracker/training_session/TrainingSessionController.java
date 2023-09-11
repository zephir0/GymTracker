package com.gymtracker.training_session;

import com.gymtracker.training_session.dto.TrainingSessionDto;
import com.gymtracker.training_session.dto.TrainingSessionResponseDto;
import com.gymtracker.training_session.service.TrainingSessionService;
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
@RequestMapping("/api/training-sessions")
@Api(tags = "Training Session API")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a new training session")
    SuccessResponse createTrainingSession(@RequestBody @Validated TrainingSessionDto trainingSessionDto) {
        trainingSessionService.createTrainingSession(trainingSessionDto);
        return new SuccessResponse(HttpStatus.CREATED, "Training session has been created", LocalDateTime.now());
    }

    @DeleteMapping("/{trainingSessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Delete a training session by ID")
    SuccessResponse deleteTrainingSession(@PathVariable Long trainingSessionId) {
        trainingSessionService.deleteTrainingSession(trainingSessionId);
        return new SuccessResponse(HttpStatus.OK, "Training session has been deleted", LocalDateTime.now());
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get all training sessions for logged user")
    List<TrainingSessionResponseDto> getAllTrainingSessionsForLoggedUser() {
        return trainingSessionService.getAllTrainingSessionsForLoggedUser();
    }

    @GetMapping("/{trainingSessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get a training session by ID")
    TrainingSessionResponseDto getTrainingSessionById(@PathVariable Long trainingSessionId) {
        return trainingSessionService.getTrainingSessionDtoById(trainingSessionId);
    }
}
