package com.gymtracker.ticket.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends ApiException {
    public TicketNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
