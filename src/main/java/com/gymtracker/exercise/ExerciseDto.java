package com.gymtracker.exercise;


import org.springframework.lang.NonNull;


public record ExerciseDto(Long id, @NonNull String name, @NonNull MuscleGroup muscleGroup) {
}
