package com.gymtracker.training_routine;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class UnAuthorizedAccessTrainingRoutineException extends ApiException {
    public UnAuthorizedAccessTrainingRoutineException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
