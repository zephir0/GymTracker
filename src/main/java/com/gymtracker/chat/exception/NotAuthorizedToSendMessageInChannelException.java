package com.gymtracker.chat.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedToSendMessageInChannelException extends ApiException {
    public NotAuthorizedToSendMessageInChannelException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
