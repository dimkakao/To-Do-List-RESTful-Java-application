package com.softserve.itacademy.todolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "error";

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        var fieldErrors = ex.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        log.info(ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.info(ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUserNameAlreadyExist(EntityAlreadyExistException ex) {
        log.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
