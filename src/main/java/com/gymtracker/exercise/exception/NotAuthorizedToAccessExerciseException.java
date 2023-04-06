package com.gymtracker.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not admin or creator of that exercise")
public class NotAuthorizedToAccessExerciseException extends RuntimeException {
    public NotAuthorizedToAccessExerciseException(String message) {
        super(message);
    }
}
