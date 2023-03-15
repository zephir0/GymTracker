package com.gymtracker.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "You need to insert an exercise description")
public class EmptyExerciseDescriptionException extends RuntimeException {
    public EmptyExerciseDescriptionException(String message) {
        super(message);
    }
}
