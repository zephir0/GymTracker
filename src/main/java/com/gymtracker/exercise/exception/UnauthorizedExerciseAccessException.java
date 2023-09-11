package com.gymtracker.exercise.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedExerciseAccessException extends ApiException {
    public UnauthorizedExerciseAccessException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
