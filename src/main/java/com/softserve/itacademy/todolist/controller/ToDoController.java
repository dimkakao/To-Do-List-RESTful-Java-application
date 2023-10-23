package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.exception.EntityAlreadyExistException;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        if (user == null) throw new EntityNotFoundException();
        List<ToDo> todos = user.getMyTodos();
        todos.addAll(user.getOtherTodos());
        return todos.stream().map(ToDoResponse::new).toList();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityCheck.isLoggedUser(#userId)")
    public ToDoResponse createTodoByUser(@PathVariable Long userId, @Valid @RequestBody ToDoDto newToDo) {
        User user = userService.readById(userId);
        Optional<ToDo> foundToDo = toDoService.getAll().stream()
                .filter(e -> e.getTitle().equals(newToDo.getTitle()))
                .findFirst();
        if (foundToDo.isPresent()) throw new EntityNotFoundException("Todo with some title already exist.");
        if (user == null) throw new EntityNotFoundException("User with " + userId + " not found.");
        ToDo toDo = new ToDo();
        toDo.setTitle(newToDo.getTitle());
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(user);
        return new ToDoResponse(toDoService.create(toDo));

    }

    @GetMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public ToDoResponse getUserTodoById(@PathVariable("userId") Long userId, @PathVariable("todoId") Long todoId) {
        return new ToDoResponse(toDoService.readById(todoId));
    }

    @PutMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public ToDoResponse updateTodoByUser(@PathVariable Long userId, @PathVariable Long todoId, @Valid @RequestBody ToDoDto newToDo) {
        ToDo toDo = toDoService.readById(todoId);
        Optional<ToDo> foundToDo = toDoService.getAll().stream()
                .filter(e -> e.getTitle().equals(newToDo.getTitle()))
                .filter(e -> !Objects.equals(e.getId(), todoId))
                .findFirst();
        if (foundToDo.isPresent()) throw new EntityNotFoundException("Todo with some title already exist.");
        toDo.setTitle(newToDo.getTitle());
        return new ToDoResponse(toDoService.update(toDo));
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String deleteTodoByUser(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.readById(todoId);
        if (toDo == null) throw new EntityNotFoundException("ToDo with " + todoId + " not found.");
        toDoService.delete(todoId);
        return "OK";
    }

    @GetMapping("/{todoId}/collaborators")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId) {
        ToDo toDo = toDoService.readById(todoId);
        List<UserResponse> userResponses = toDo.getCollaborators().stream()
                .map(UserResponse::new)
                .toList();
        return userResponses;
    }

    @PostMapping("/{todoId}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String postTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId, @RequestBody CollaboratorDto user) {
        User collaborator = userService.readById(user.getId());
        ToDo toDo = toDoService.readById(todoId);
        if (toDo.getCollaborators().contains(collaborator))
            throw new EntityAlreadyExistException("Collaborator already added.");
        toDo.getCollaborators().add(collaborator);
        toDoService.update(toDo);
        return "OK";
    }

    @DeleteMapping("/{todoId}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (@securityCheck.isLoggedUser(#userId) and @securityCheck.isOwner(#todoId))")
    public String deleteTodoCollaborators(@PathVariable Long userId, @PathVariable Long todoId, @RequestBody CollaboratorDto user) {
        User collaborator = userService.readById(user.getId());
        ToDo toDo = toDoService.readById(todoId);
        if (toDo == null) throw new EntityNotFoundException();
        if (!toDo.getCollaborators().contains(collaborator)) {
            throw new EntityNotFoundException("This user is not collaborator.");
        }
        toDo.getCollaborators().remove(collaborator);
        toDoService.update(toDo);
        return "OK";
    }
}
