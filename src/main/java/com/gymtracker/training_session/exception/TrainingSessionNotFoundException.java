package com.gymtracker.training_session.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TrainingSessionNotFoundException extends ApiException {
    public TrainingSessionNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
