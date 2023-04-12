package com.gymtracker.training_log.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This gym diary doesn't contain any logs")
public class TrainingLogNotFoundException extends RuntimeException {
    public TrainingLogNotFoundException(String message) {
        super(message);
    }
}
