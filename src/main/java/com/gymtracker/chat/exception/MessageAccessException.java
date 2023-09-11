package com.gymtracker.chat.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MessageAccessException extends ApiException {
    public MessageAccessException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
