package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoDto {
    @NotBlank(message = "The 'title' cannot be empty")
    private String title;

    public ToDoDto() {
    }

    public ToDoDto(String title) {
        this.title = title;
    }
}