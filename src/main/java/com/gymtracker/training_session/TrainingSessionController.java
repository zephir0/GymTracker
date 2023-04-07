package com.gymtracker.training_session;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/training-sessions")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @PostMapping
    ResponseEntity<String> createDiary(@RequestBody @Valid TrainingSessionDto trainingSessionDto) {
        trainingSessionService.createTrainingSession(trainingSessionDto);
        return new ResponseEntity<>("Gym diary has been created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<String> editDiary(@PathVariable Long id,
                                     @Valid @RequestBody TrainingSessionDto trainingSessionDto) {
        trainingSessionService.editTrainingSession(id, trainingSessionDto);
        return new ResponseEntity<>("Gym diare has been deleted", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        trainingSessionService.deleteTrainingSession(id);
        return new ResponseEntity<>("Gym diare has been deleted", HttpStatus.OK);
    }

    @GetMapping("/user")
    ResponseEntity<List<TrainingSession>> getAllGymDiariesForLoggedUser() {
        List<TrainingSession> gymDiaries = trainingSessionService.getAllTrainingSessionsForLoggedUser();
        return new ResponseEntity<>(gymDiaries, HttpStatus.OK);
    }

}
