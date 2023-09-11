package com.gymtracker.training_routine;

import com.gymtracker.exercise.dto.ExerciseDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record TrainingRoutineDto(@NotNull String routineName, @NotNull List<ExerciseDto> exerciseList) {
}
