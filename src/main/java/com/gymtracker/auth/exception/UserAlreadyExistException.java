package com.gymtracker.auth.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ApiException {
    public UserAlreadyExistException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
