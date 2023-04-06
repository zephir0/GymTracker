package com.gymtracker.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not authorized to get messages in that ticket channel")
public class NotAuthorizedToGetMessagesException extends RuntimeException {
    public NotAuthorizedToGetMessagesException(String message) {
        super(message);
    }
}
