package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskRequest;
import com.softserve.itacademy.todolist.dto.TaskResponse;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import com.softserve.itacademy.todolist.util.EntityNotFoundMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final ToDoService toDoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, UserService userService, ToDoService toDoService, StateService stateService) {
        this.taskService = taskService;
        this.userService = userService;
        this.toDoService = toDoService;
        this.stateService = stateService;
    }

    @GetMapping("/{userId}/todos/{todoId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskResponse>> getTodoTasks(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.readById(todoId);
        if (toDo == null ) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
        List<TaskResponse> taskResponses = toDo.getTasks().stream()
                .map(TaskResponse::new)
                .toList();
        return new ResponseEntity<>(taskResponses, HttpStatus.OK);
    }

    @PostMapping("/{userId}/todos/{todoId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> createTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @Valid @RequestBody TaskRequest newTask) {
        Task task = new Task();
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().toUpperCase()));
        task.setTodo(toDoService.readById(todoId));
        task.setState(stateService.getByName("NEW"));
        TaskResponse taskResponse = new TaskResponse(taskService.create(task));
        return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/todos/{todoId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> getTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @PathVariable Long taskId) {
        Task task = taskService.readById(taskId);
        if (toDoService.readById(todoId) == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
        if (task == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
        TaskResponse taskResponse = new TaskResponse(task);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @PutMapping("/{userId}/todos/{todoId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> updateTodoTask(@PathVariable Long userId, @PathVariable Long todoId,
                                @PathVariable Long taskId, @Valid @RequestBody TaskRequest newTask) {
        Task task = taskService.readById(taskId);
        if (toDoService.readById(todoId) == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
        if (task == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().toUpperCase()));
        task.setState(stateService.readById(newTask.getStateId()));
        taskService.update(task);
        TaskResponse taskResponse = new TaskResponse(task);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/todos/{todoId}/tasks/{taskId}")
    public ResponseEntity<String> deleteTodoTask(@PathVariable Long userId, @PathVariable Long todoId, @PathVariable Long taskId) {
        Task oldTask = taskService.readById(taskId);
        if (toDoService.readById(todoId) == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todoId));
        if (oldTask == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("Task", taskId));
        taskService.delete(taskId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
