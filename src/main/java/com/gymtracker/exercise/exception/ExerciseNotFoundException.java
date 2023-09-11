package com.gymtracker.exercise.exception;

import com.gymtracker.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExerciseNotFoundException extends ApiException {

    public ExerciseNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
