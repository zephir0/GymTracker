package com.gymtracker.exercise;

import com.gymtracker.exercise.exception.ExerciseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public void createExercise(ExerciseDto exerciseDto) {
        exerciseRepository.save(exerciseMapper.toEntity(exerciseDto));
    }

    public void editExercise(Long id,
                             ExerciseDto exerciseDto) {
        exerciseRepository.findById(id).ifPresentOrElse(exercise -> {
            exercise.setDescription(exerciseDto.description());
            exercise.setMuscleGroup(exerciseDto.muscleGroup());
            exerciseRepository.save(exercise);
        }, () -> {
            throw new ExerciseNotFoundException("Exercise not found");
        });
    }

    public void deleteExercise(Long id) {
        exerciseRepository.findById(id).ifPresentOrElse(exerciseRepository::delete, () -> {
            throw new ExerciseNotFoundException("Exercise not found");
        });
    }


    public boolean existById(Long id) {
        return exerciseRepository.existsById(id);
    }
}
