package com.gymtracker.user.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class IncorrectOldPasswordException extends ApiException {
    public IncorrectOldPasswordException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
