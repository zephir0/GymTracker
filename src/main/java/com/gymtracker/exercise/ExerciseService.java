package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.EmptyExerciseDescriptionException;
import com.gymtracker.exercise.exception.EmptyExerciseMuscleGroupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public void createExercise(ExerciseDto exerciseDto) {
        if (exerciseDto.description().isEmpty())
            throw new EmptyExerciseDescriptionException("You need to insert an exercise description");

        if (exerciseDto.muscleGroup() == null)
            throw new EmptyExerciseMuscleGroupException("You need to insert an exercise muscle group");

        exerciseRepository.save(exerciseMapper.toEntity(exerciseDto));
    }
}
