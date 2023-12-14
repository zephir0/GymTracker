package com.gymtracker.user.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAddressTakenException extends ApiException {
    public EmailAddressTakenException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
