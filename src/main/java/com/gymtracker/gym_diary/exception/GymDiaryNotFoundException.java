package com.gymtracker.gym_diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Gym diary doesn't exist in database")
public class GymDiaryNotFoundException extends RuntimeException{
    public GymDiaryNotFoundException(String message) {
        super(message);
    }
}
