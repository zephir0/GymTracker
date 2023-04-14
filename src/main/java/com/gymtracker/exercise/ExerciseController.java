package com.gymtracker.exercise;

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
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;


    @PostMapping
    public ResponseEntity<SuccessResponse> createExercise(@RequestBody @Validated ExerciseDto exerciseDto) {
        exerciseService.createExercise(exerciseDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Exercise created successfully", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> editExercise(@PathVariable Long id,
                                                        @RequestBody @Validated ExerciseDto exerciseDto) {
        exerciseService.editExercise(id, exerciseDto);

        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Exercise updated successfully", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<SuccessResponse> getExercises() {
        List<ExerciseResponseDto> allExercises = exerciseService.getAllExercises();
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, allExercises, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Exercise deleted successfully", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
