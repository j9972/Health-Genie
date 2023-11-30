package com.example.healthgenie.boundedContext.todo.repository;

import com.example.healthgenie.boundedContext.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByMemberIdAndDate(Long userId, LocalDate date);
}
