package com.gymtracker.exercise;

import java.util.List;

public interface ExerciseService {
    void createExercise(ExerciseDto exerciseDto);

    void editExercise(Long id,
                      ExerciseDto exerciseDto);

    void deleteExercise(Long id);

    List<ExerciseResponseDto> getAllExercises();

    boolean existById(Long id);

    boolean isAuthorized(Exercise exercise);
}
