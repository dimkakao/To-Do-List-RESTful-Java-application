package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    @Query(value =
            """
                SELECT t.*
                FROM todos t
                WHERE t.owner_id = :userId
                UNION
                SELECT t.*
                FROM todos t
                INNER JOIN todo_collaborator tc ON t.id = tc.todo_id
                WHERE tc.collaborator_id = :userId
            """,
            nativeQuery = true)
    List<ToDo> getByUserId(long userId);
}
