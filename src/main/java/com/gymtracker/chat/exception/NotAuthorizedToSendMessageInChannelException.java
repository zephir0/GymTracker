package com.gymtracker.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not authorized to send message in that channel")
public class NotAuthorizedToSendMessageInChannelException extends RuntimeException {
    public NotAuthorizedToSendMessageInChannelException(String message) {
        super(message);
    }
}
