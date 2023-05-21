package com.gymtracker.training_session;

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
@RequestMapping("/api/training-sessions")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @PostMapping
    ResponseEntity<SuccessResponse> createDiary(@RequestBody @Validated TrainingSessionDto trainingSessionDto) {
        trainingSessionService.createTrainingSession(trainingSessionDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Training session has been created", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<SuccessResponse> editDiary(@PathVariable Long id,
                                              @RequestBody @Validated TrainingSessionDto trainingSessionDto) {
        trainingSessionService.editTrainingSession(id, trainingSessionDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training session has been deleted", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<SuccessResponse> deleteDiary(@PathVariable Long id) {
        trainingSessionService.deleteTrainingSession(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Training session has been deleted", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    ResponseEntity<List<TrainingSessionResponseDto>> getAllGymDiariesForLoggedUser() {
        List<TrainingSessionResponseDto> gymDiaries = trainingSessionService.getAllTrainingSessionsForLoggedUser();
        return new ResponseEntity<>(gymDiaries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<TrainingSessionResponseDto> getTrainingSessionById(@PathVariable Long id) {
        TrainingSessionResponseDto trainingSessionById = trainingSessionService.getTrainingSessionDtoById(id);
        return new ResponseEntity<>(trainingSessionById, HttpStatus.OK);
    }

}
