package com.gymtracker.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private HttpStatus httpStatus;
    private Object body;
    private LocalDateTime timeStamp;
}
