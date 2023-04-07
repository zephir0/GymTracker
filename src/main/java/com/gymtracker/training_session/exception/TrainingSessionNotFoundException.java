package com.gymtracker.training_session.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Gym diary doesn't exist in database")
public class TrainingSessionNotFoundException extends RuntimeException{
    public TrainingSessionNotFoundException(String message) {
        super(message);
    }
}
