package com.softserve.itacademy.todolist.util;



public class EntityNotFoundMessage {
    public static String notfoundMessage(String entityName, long id) {
        return entityName + " with id " + id + " not found";
    }
}
