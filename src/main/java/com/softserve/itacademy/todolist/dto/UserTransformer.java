package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer {

    private final RoleService roleService;

    public UserTransformer(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserDto convertUserToUserDto(User user) {
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().getId()
        );
    }

    public User convertUserDtoToUser(UserDto userDto, User user) {
        if (user == null) {
            user = new User();
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(roleService.readById(userDto.getRoleId()));
        return user;
    }
}
