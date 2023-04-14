package com.gymtracker.exercise.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class EmptyExerciseDescriptionException extends ApiException {
    public EmptyExerciseDescriptionException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
    }
}
