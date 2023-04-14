package com.gymtracker.exercise;


import org.springframework.lang.NonNull;


public record ExerciseDto(@NonNull String description, @NonNull MuscleGroup muscleGroup) {
}
