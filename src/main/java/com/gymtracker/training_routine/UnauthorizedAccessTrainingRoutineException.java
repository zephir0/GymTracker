package com.gymtracker.training_routine;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedAccessTrainingRoutineException extends ApiException {
    public UnauthorizedAccessTrainingRoutineException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
