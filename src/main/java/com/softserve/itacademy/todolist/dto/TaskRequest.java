package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskRequest {

    @NotBlank(message = "The 'name' cannot be empty")
    String name;

    String priority;
    @JsonProperty("state_id")
    Long stateId;

    public TaskRequest() {
    }

    public TaskRequest(String name, String priority, Long stateId) {
        this.name = name;
        this.priority = priority;
        this.stateId = stateId;
    }
}


