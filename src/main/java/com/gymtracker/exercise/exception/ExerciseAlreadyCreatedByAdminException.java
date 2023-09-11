package com.gymtracker.exercise.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExerciseAlreadyCreatedByAdminException extends ApiException {
    public ExerciseAlreadyCreatedByAdminException(
            String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
