package com.gymtracker.training_routine.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TrainingRoutineNotFoundException extends ApiException {
    public TrainingRoutineNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
