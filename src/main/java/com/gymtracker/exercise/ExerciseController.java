package com.gymtracker.exercise;

import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.dto.ExerciseResponseDto;
import com.gymtracker.exercise.service.ExerciseService;
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
@RequestMapping("/api/exercises")
@Api(tags = "Exercise API")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @ApiOperation("Create a new exercise")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createExercise(@RequestBody @Validated ExerciseDto exerciseDto) {
        exerciseService.createExercise(exerciseDto);
        return new SuccessResponse(HttpStatus.CREATED, "Exercise created successfully", LocalDateTime.now());
    }

    @ApiOperation("Edit an existing exercise")
    @PutMapping("/{exerciseId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse editExercise(@PathVariable Long exerciseId,
                                        @RequestBody @Validated ExerciseDto exerciseDto) {
        exerciseService.editExercise(exerciseId, exerciseDto);
        return new SuccessResponse(HttpStatus.OK, "Exercise updated successfully", LocalDateTime.now());
    }

    @ApiOperation("Get list of all exercises")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciseResponseDto> getExercises() {
        return exerciseService.getAllExercises();
    }

    @ApiOperation("Delete an exercise")
    @DeleteMapping("/{exerciseId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deleteExercise(@PathVariable Long exerciseId) {
        exerciseService.deleteExercise(exerciseId);
        return new SuccessResponse(HttpStatus.OK, "Exercise deleted successfully", LocalDateTime.now());
    }
}
