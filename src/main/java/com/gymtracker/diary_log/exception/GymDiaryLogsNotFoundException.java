package com.gymtracker.diary_log.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This gym diary doesn't contain any logs")
public class GymDiaryLogsNotFoundException extends RuntimeException {
    public GymDiaryLogsNotFoundException(String message) {
        super(message);
    }
}
