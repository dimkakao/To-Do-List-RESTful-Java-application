package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("from Task where todo.id = :todoId")
    Optional<List<Task>> getByTodoId(long todoId);
}
