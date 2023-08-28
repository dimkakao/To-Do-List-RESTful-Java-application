package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserDto;
import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.dto.UserTransformer;
import com.softserve.itacademy.todolist.exception.EntityAlreadyExistException;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer;

    @Autowired
    public UserController(UserService userService, UserTransformer userTransformer) {
        this.userService = userService;
        this.userTransformer = userTransformer;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> users =  userService.getAll().stream()
                .map(UserResponse::new)
                .toList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse =  new UserResponse(userService.readById(id));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createNewUser (@Valid @RequestBody UserDto userDto) {
        UserDetails existingUser = userService.loadUserByUsername(userDto.getEmail());
        if (existingUser != null) throw new EntityAlreadyExistException("User with email " + userDto.getEmail() + " already exist.");

        User user = userService.create(userTransformer.convertUserDtoToUser(userDto, null));
        UserResponse userResponse = new UserResponse(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> updateUser (@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User updatedUser = userService.readById(id);
        updatedUser = userTransformer.convertUserDtoToUser(userDto, updatedUser);
        User user = userService.update(updatedUser);
        UserResponse userResponse = new UserResponse(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
