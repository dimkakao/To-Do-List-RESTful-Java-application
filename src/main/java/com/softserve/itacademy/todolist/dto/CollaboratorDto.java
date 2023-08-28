package com.softserve.itacademy.todolist.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollaboratorDto {
    private long id;

    public CollaboratorDto() {
    }

    public CollaboratorDto(long id) {
        this.id = id;
    }
}
