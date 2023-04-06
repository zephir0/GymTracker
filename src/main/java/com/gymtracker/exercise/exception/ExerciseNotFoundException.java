package com.gymtracker.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exercise was not found.")
public class ExerciseNotFoundException extends RuntimeException {

    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
