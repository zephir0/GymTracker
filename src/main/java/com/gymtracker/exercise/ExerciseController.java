package com.gymtracker.exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;


    @PostMapping
    public ResponseEntity<String> createExercise(@RequestBody ExerciseDto exerciseDto) {
        exerciseService.createExercise(exerciseDto);
        return new ResponseEntity<>("Exercise created successfully", HttpStatus.CREATED);
    }
}
