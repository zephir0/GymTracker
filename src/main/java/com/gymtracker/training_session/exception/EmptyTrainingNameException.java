package com.gymtracker.training_session.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "You need to insert a training name")

public class EmptyTrainingNameException extends RuntimeException {
    public EmptyTrainingNameException(String message) {
        super(message);
    }
}
