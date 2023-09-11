package com.gymtracker.training_routine.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedTrainingRoutineAccessException extends ApiException {
    public UnauthorizedTrainingRoutineAccessException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
