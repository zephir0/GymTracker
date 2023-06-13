package com.gymtracker.exercise.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedToAccessExerciseException extends ApiException {
    public NotAuthorizedToAccessExerciseException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
