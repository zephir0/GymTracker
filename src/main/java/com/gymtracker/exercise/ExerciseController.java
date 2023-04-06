package com.gymtracker.exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;


    @PostMapping
    public ResponseEntity<String> createExercise(@RequestBody @Valid ExerciseDto exerciseDto) {
        exerciseService.createExercise(exerciseDto);
        return new ResponseEntity<>("Exercise created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editExercise(@PathVariable Long id,
                                               @RequestBody ExerciseDto exerciseDto) {
        exerciseService.editExercise(id, exerciseDto);
        return new ResponseEntity<>("Exercise deleted successfully", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Exercise>> getExercises() {
        List<Exercise> allExercises = exerciseService.getAllExercises();
        return new ResponseEntity<>(allExercises, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return new ResponseEntity<>("Exercise deleted successfully", HttpStatus.OK);
    }
}
