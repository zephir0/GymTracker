package com.gymtracker.exercise.service;

import com.gymtracker.exercise.entity.Exercise;
import com.gymtracker.exercise.dto.ExerciseDto;
import com.gymtracker.exercise.dto.ExerciseResponseDto;

import java.util.List;

public interface ExerciseService {
    void createExercise(ExerciseDto exerciseDto);

    void editExercise(Long id,
                      ExerciseDto exerciseDto);

    void deleteExercise(Long id);

    List<ExerciseResponseDto> getAllExercises();

    boolean isAuthorized(Exercise exercise);
}
