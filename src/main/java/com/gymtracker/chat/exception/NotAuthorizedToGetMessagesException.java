package com.gymtracker.chat.exception;

import com.gymtracker.utils.ApiException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedToGetMessagesException extends ApiException {
    public NotAuthorizedToGetMessagesException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
