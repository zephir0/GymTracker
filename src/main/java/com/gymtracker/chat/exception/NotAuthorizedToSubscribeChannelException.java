package com.gymtracker.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not authorized to join channel")
public class NotAuthorizedToSubscribeChannelException extends RuntimeException {
    public NotAuthorizedToSubscribeChannelException(String message) {
        super(message);
    }
}
