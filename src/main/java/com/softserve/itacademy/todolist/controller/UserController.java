package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserDto;
import com.softserve.itacademy.todolist.dto.requestsAndResponce.UserResponse;
import com.softserve.itacademy.todolist.dto.UserTransformer;
import com.softserve.itacademy.todolist.exception.EntityAlreadyExistException;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityCheck.isLoggedUser(#id)")
    public UserResponse getUserById(@PathVariable Long id) {
        return new UserResponse(userService.readById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createNewUser(@Valid @RequestBody UserDto userDto) {
        try {
            userService.loadUserByUsername(userDto.getEmail());
            throw new EntityAlreadyExistException("User with email " + userDto.getEmail() + " already exist.");
        } catch (EntityNotFoundException e) {
            User user = userService.create(userTransformer.convertUserDtoToUser(userDto, null));
            return new UserResponse(user);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityCheck.isLoggedUser(#id)")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User updatedUser = userService.readById(id);
        updatedUser = userTransformer.convertUserDtoToUser(userDto, updatedUser);
        User user = userService.update(updatedUser);
        return new UserResponse(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "OK";
    }
}
