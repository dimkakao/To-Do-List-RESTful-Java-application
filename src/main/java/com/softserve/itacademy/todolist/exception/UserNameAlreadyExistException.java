package com.softserve.itacademy.todolist.exception;

public class UserNameAlreadyExistException extends RuntimeException {
    public UserNameAlreadyExistException() {    }

    public UserNameAlreadyExistException(String message) {
        super(message);
    }
}
