package com.gymtracker.training_routine;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedAccessTrainingRoutineException extends ApiException {
    public NotAuthorizedAccessTrainingRoutineException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
