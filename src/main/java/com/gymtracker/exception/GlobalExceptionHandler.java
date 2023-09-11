package com.gymtracker.exception;

import response_model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ErrorResponse(HttpStatus.BAD_REQUEST, errors, LocalDateTime.now());
    }


    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleUnsupportedMediaTypeException(HttpMediaTypeNotAcceptableException ex) {
        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), LocalDateTime.now());
    }


    @ExceptionHandler(TransientPropertyValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTransientPropertyValueException(TransientPropertyValueException ex) {
        String propertyName = ex.getPropertyName() + "Id";
        return new ErrorResponse(HttpStatus.BAD_REQUEST, Map.of(propertyName, "Must not be null"), LocalDateTime.now());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ConstraintViolationException ex) {
        Map<String, String> fieldErrors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage));
        return new ErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors, LocalDateTime.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleValidationException() {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid login or password", LocalDateTime.now());

    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleValidationException(EntityNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(ApiException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        String message = extractMessage(ex);

        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE, message, LocalDateTime.now());
    }

    private String extractMessage(SQLIntegrityConstraintViolationException ex) {
        String errorMessage = ex.getMessage();

        if (errorMessage.contains("`exercise_id`") && errorMessage.contains("`exercise`")) {
            return "The provided exercise ID does not exist in the database.";
        } else if (errorMessage.contains("`routine_id`") && errorMessage.contains("`routine`")) {
            return "The provided routine ID does not exist in the database.";
        }
        return "There was an issue processing your request.";
    }

}
