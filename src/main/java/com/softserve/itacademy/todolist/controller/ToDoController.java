package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.dto.requestsAndResponce.ToDoResponse;
import com.softserve.itacademy.todolist.dto.requestsAndResponce.UserResponse;
import com.softserve.itacademy.todolist.exception.EntityAlreadyExistException;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;

import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users/{userId}/todos")
public class ToDoController {

    private final UserService userService;
    private final ToDoService toDoService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityCheck.isLoggedUser(#userId)")
    public List<ToDoResponse> getTodosByUser(@PathVariable Long userId) {
        User user = userService.readById(userId);
        List<ToDo> todos = user.getMyTodos();
        todos.addAll(user.getOtherTodos());
        return todos.stream().map(ToDoResponse::new).toList();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityCheck.isLoggedUser(#userId)")
    public ToDoResponse createTodoByUser(@PathVariable Long userId, @Valid @RequestBody ToDoDto newToDo) {
        User user = userService.readById(userId);
        Optional<ToDo> foundToDo = user.getMyTodos().stream().filter(e -> e.getTitle().equals(newToDo.getTitle())).findFirst();
        if (foundToDo.isPresent()) {
            throw new EntityAlreadyExistException("Todo with some title already exist.");
        }
        ToDo toDo = new ToDo();
        toDo.setTitle(newToDo.getTitle());
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(user);
        return new ToDoResponse(toDoService.create(toDo));
    }

    @GetMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("(@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId)) or hasAuthority('ROLE_ADMIN')")
    public ToDoResponse getUserTodoById(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId) {
        ToDo toDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        return new ToDoResponse(toDo);
    }

    @PutMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public ToDoResponse updateTodoByUser(@PathVariable Long userId, @PathVariable Long todoId, @Valid @RequestBody ToDoDto newToDo) {
        ToDo foundToDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        foundToDo.setTitle(newToDo.getTitle());
        return new ToDoResponse(toDoService.update(foundToDo));
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String deleteTodoByUser(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        toDoService.delete(todoId);
        return "OK";
    }

    @GetMapping("/{todoId}/collaborators")
    @PreAuthorize("@securityCheck.isLoggedOwnerOrCollaborator(#userId, #todoId) or hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        return toDo.getCollaborators().stream().map(UserResponse::new).toList();
    }

    @PostMapping("/{todoId}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String postTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId, @RequestBody CollaboratorDto user) {
        User collaborator = userService.readById(user.getId());
        ToDo toDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        if (toDo.getCollaborators().contains(collaborator)) {
            throw new EntityAlreadyExistException("Collaborator already added.");
        }
        toDo.getCollaborators().add(collaborator);
        toDoService.update(toDo);
        return "OK";
    }

    @DeleteMapping("/{todoId}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String deleteTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId, @RequestBody CollaboratorDto user) {
        User collaborator = userService.readById(user.getId());
        ToDo toDo = toDoService.getTodoByIdAndUserId(todoId, userId);
        if (!toDo.getCollaborators().contains(collaborator)) {
            throw new EntityNotFoundException("This user is not collaborator.");
        }
        toDo.getCollaborators().remove(collaborator);
        toDoService.update(toDo);
        return "OK";
    }
}