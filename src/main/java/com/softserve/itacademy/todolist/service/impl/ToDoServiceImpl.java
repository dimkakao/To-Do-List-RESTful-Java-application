package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.ToDoRepository;
import com.softserve.itacademy.todolist.repository.UserRepository;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.util.EntityNotFoundMessage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;
    private final UserRepository userRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository,
                           UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        if (todo == null) {
            throw new NullEntityReferenceException("ToDo cannot be 'null'");
        }
        return todoRepository.save(todo);
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", id)));
    }
    @Override
    public ToDo update(ToDo todo) {
        if (todo == null) throw new NullEntityReferenceException("ToDo cannot be 'null'");
        ToDo findedToDo = readById(todo.getId());
        if (findedToDo == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", todo.getId()));
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long id) {
        ToDo todo = readById(id);
        if (todo == null) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("ToDo", id));
        todoRepository.delete(todo);
    }

    @Override
    public List<ToDo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new EntityNotFoundException(EntityNotFoundMessage.notfoundMessage("User", userId));
        return todoRepository.getByUserId(userId);
    }
}
