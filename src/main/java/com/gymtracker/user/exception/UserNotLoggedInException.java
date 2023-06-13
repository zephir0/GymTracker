package com.gymtracker.user.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends ApiException {
    public UserNotLoggedInException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
