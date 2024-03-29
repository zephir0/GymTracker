package com.gymtracker.training_session.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedTrainingSessionAccessException extends ApiException {
    public UnauthorizedTrainingSessionAccessException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
