package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskRequest;
import com.softserve.itacademy.todolist.dto.TaskResponse;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.util.EntityNotFoundMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/todos/{todoId}/tasks")
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final ToDoService toDoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, ToDoService toDoService, StateService stateService) {
        this.taskService = taskService;
        this.toDoService = toDoService;
        this.stateService = stateService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or" +
//            " (@securityCheck.isLoggedUser(#userId) and" +
//            "(@securityCheck.isOwner(#todoId) or @securityCheck.isCollaborator(#todoId)))")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or " + "@securityCheck.isAdminOrIsLoggedOwnerOrCollaborator(#userId, #todoId)")
    public List<TaskResponse> getTodoTasks(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.readById(todoId);
        if (toDo == null ) {
            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
        }
        return toDo.getTasks().stream()
                .map(TaskResponse::new)
                .toList();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or " + "@securityCheck.isAdminOrIsLoggedOwnerOrCollaborator(#userId, #todoId)")
    public TaskResponse createTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @Valid @RequestBody TaskRequest newTask) {
        Task task = new Task();
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().toUpperCase()));
        task.setTodo(toDoService.readById(todoId));
        task.setState(stateService.getByName("NEW"));
        return new TaskResponse(taskService.create(task));
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or " + "@securityCheck.isAdminOrIsLoggedOwnerOrCollaborator(#userId, #todoId)")
    public TaskResponse getTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @PathVariable Long taskId) {
        Task task = taskService.readById(taskId);
//        if (toDoService.readById(todoId) == null) {
//            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA");
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
//        }
//        if (task == null) {
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
//        }
        log.info("Task was received correctly");
        return new TaskResponse(task);
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or " + "@securityCheck.isAdminOrIsLoggedOwnerOrCollaborator(#userId, #todoId)")
    public TaskResponse updateTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @PathVariable Long taskId, @Valid @RequestBody TaskRequest newTask) {
        Task task = taskService.readById(taskId);
//        if (toDoService.readById(todoId) == null) {
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
//        }
//        if (task == null) {
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
//        }
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().toUpperCase()));
        task.setState(stateService.readById(newTask.getStateId()));
        taskService.update(task);
        return new TaskResponse(task);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or" +
            " (@securityCheck.isLoggedUser(#userId) and" +
            "(@securityCheck.isOwner(#todoId)))")
    public String deleteTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @PathVariable Long taskId) {
        Task oldTask = taskService.readById(taskId);
//        if (toDoService.readById(todoId) == null) {
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
//        }
//        if (oldTask == null) {
//            throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
//        }
        taskService.delete(taskId);
        return "OK";
    }
}
