package com.gymtracker.exercise.dto;

import com.gymtracker.exercise.entity.MuscleGroup;

public record ExerciseResponseDto(String name, MuscleGroup muscleGroup) {
}
