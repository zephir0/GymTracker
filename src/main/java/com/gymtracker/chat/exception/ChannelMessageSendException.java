package com.gymtracker.chat.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ChannelMessageSendException extends ApiException {
    public ChannelMessageSendException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
