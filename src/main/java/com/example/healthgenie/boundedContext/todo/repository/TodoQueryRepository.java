package com.example.healthgenie.boundedContext.todo.repository;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.healthgenie.boundedContext.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {
    private final JPAQueryFactory query;

    public List<Todo> findAllByMemberIdAndDate(Long userId, LocalDate date) {
        return query
                .selectFrom(todo)
                .where(todo.member.id.eq(userId)
                        .and(todo.date.eq(date)))
                .orderBy(todo.id.desc())
                .fetch();
    }

}
