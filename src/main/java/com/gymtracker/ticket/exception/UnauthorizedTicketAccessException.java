package com.gymtracker.ticket.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedTicketAccessException extends ApiException {
    public UnauthorizedTicketAccessException(
            String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
