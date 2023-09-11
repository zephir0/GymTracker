package com.gymtracker.training_log.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TrainingLogNotFoundException extends ApiException {
    public TrainingLogNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
