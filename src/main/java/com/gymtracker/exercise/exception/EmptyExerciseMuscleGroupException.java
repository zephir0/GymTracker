package com.gymtracker.exercise.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "You need to insert an exercise muscle group")
public class EmptyExerciseMuscleGroupException extends RuntimeException {
    public EmptyExerciseMuscleGroupException(String message) {
        super(message);
    }
}
