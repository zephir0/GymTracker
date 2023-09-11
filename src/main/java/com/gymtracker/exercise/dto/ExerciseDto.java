package com.gymtracker.exercise.dto;


import com.gymtracker.exercise.entity.MuscleGroup;

import javax.validation.constraints.NotNull;


public record ExerciseDto(Long id, @NotNull String name, @NotNull MuscleGroup muscleGroup) {
}
