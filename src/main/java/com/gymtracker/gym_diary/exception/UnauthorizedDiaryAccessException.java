package com.gymtracker.gym_diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "You are not a diary creator or admin")
public class UnauthorizedDiaryAccessException extends RuntimeException {
    public UnauthorizedDiaryAccessException(String message) {
        super(message);
    }
}
