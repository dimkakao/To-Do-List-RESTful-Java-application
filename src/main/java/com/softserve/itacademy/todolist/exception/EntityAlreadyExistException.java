package com.softserve.itacademy.todolist.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
