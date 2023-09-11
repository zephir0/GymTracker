package com.gymtracker.chat.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ChannelSubscriptionException extends ApiException {
    public ChannelSubscriptionException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
