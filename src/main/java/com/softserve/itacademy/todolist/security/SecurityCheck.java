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

    public boolean isOwner(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            long userId = userDetails.getId();
            ToDo todo = toDoService.readById(id);
            result = todo != null && todo.getOwner().getId() == userId;
        }
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
        return result;
    }

    public boolean isLoggedUser(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            result = userDetails.getId() == id;
        }
        return result;
    }

    public String isAdminOrIsLoggedOwnerOrCollaborator(long userId, long todoId) {
        return "%s and %s or %s".formatted(isLoggedUser(userId), isOwner(todoId), isCollaborator(todoId));
//                " (@securityCheck.isLoggedUser(#userId) and" +
//                "(@securityCheck.isOwner(#todoId) or @securityCheck.isCollaborator(#todoId)))"
    }
}
