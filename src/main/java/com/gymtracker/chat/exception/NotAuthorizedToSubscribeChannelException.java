package com.gymtracker.chat.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedToSubscribeChannelException extends ApiException {
    public NotAuthorizedToSubscribeChannelException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
