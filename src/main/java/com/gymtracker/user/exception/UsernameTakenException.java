package com.gymtracker.user.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UsernameTakenException extends ApiException {
    public UsernameTakenException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
