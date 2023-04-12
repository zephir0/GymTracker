package com.gymtracker.training_session.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not a diary creator or admin")
public class UnauthorizedTrainingSessionAccessException extends RuntimeException {
    public UnauthorizedTrainingSessionAccessException(String message) {
        super(message);
    }
}
