package com.softserve.itacademy.todolist.dto.requestsAndResponce;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.ToDo;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoResponse {
    Long id;
    String title;
    LocalDateTime createdAt;
    UserResponse owner;

    public ToDoResponse(ToDo toDo) {
        id = toDo.getId();
        title = toDo.getTitle();
        createdAt = toDo.getCreatedAt();
        owner = new UserResponse(toDo.getOwner());
    }
}
