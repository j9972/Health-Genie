package com.example.healthgenie.boundedContext.routine.repository;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.routine.entity.QRoutine.routine;

@Repository
@RequiredArgsConstructor
public class RoutineQueryRepository {
    private final JPAQueryFactory query;

    public List<Routine> findAllByLevelAndDay(Level level, Day day) {
        return query
                .selectFrom(routine)
                .where(routine.level.eq(level)
                        .and(routine.day.eq(day)))
                .orderBy(routine.id.desc())
                .fetch();
    }

    public List<Routine> findAllByLevel(Level level) {
        return query
                .selectFrom(routine)
                .where(routine.level.eq(level))
                .orderBy(routine.id.desc())
                .fetch();
    }

    public List<Routine> findAllByMemberId(Long userId) {
        return query
                .selectFrom(routine)
                .where(routine.member.id.eq(userId))
                .orderBy(routine.id.desc())
                .fetch();
    }

    public List<Routine> findAllByMemberIdAndDay(Long userId, Day day) {
        return query
                .selectFrom(routine)
                .where(routine.member.id.eq(userId)
                        .and(routine.day.eq(day)))
                .orderBy(routine.id.desc())
                .fetch();
    }
}
