package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class SecurityCheck {

    @Autowired
    private ToDoService toDoService;

    public boolean isOwner(long todoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            long userId = userDetails.getId();
            ToDo todo = toDoService.readById(todoId);
            result = todo != null && todo.getOwner().getId() == userId;
        }
        System.out.println("Is owner --- " +  result);
        return result;
    }

    public boolean isCollaborator(long todoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            ToDo todo = toDoService.readById(todoId);
            result = todo != null && todo.getCollaborators().contains(userDetails);
        }
        System.out.println("Is collaborator --- " +  result);
        return result;
    }

    public boolean isLoggedUser(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            System.out.println("Logged - " + userDetails);
            result = userDetails.getId() == id;
        }
        System.out.println("Is logged --- " +  result);
        return result;
    }

    public boolean isLoggedOwnerOrCollaborator(long userId, long todoId) {
        return isLoggedUser(userId) && (isOwner(todoId) || isCollaborator(todoId));
    }
}
