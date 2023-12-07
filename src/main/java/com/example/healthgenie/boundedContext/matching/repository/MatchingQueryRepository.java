package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.healthgenie.boundedContext.matching.entity.QMatching.matching;

@Repository
@RequiredArgsConstructor
public class MatchingQueryRepository {

    private final JPAQueryFactory query;

    /**
     *
     */
    public List<Matching> findAllForOneDayByDateAndIds(LocalDateTime date, Long userId, Long trainerId) {
        LocalDateTime startOfDay = getStartOfDay(date);
        LocalDateTime endOfDay = getEndOfDay(date);

        return query
                .selectFrom(matching)
                .where(
                        dateBetween(startOfDay, endOfDay),
                        memberIdEq(userId),
                        trainerIdEq(trainerId),
                        stateNotEq(MatchingState.CANCEL_ACCEPT)
                )
                .orderBy(matching.date.asc())
                .fetch();
    }

    private BooleanExpression stateNotEq(MatchingState state) {
        return matching.state.ne(state);
    }

    public Matching findOne(LocalDateTime date, Long userId, Long trainerId) {
        return query
                .selectFrom(matching)
                .where(
                        dateEq(date),
                        memberIdEq(userId),
                        trainerIdEq(trainerId)
                )
                .fetchOne();
    }

    private BooleanExpression dateEq(LocalDateTime date) {
        return matching.date.eq(date);
    }

    private BooleanExpression memberIdEq(Long userId) {
        return matching.member.id.eq(userId);
    }

    private BooleanExpression trainerIdEq(Long trainerId) {
        return matching.trainer.id.eq(trainerId);
    }

    private BooleanExpression dateBetween(LocalDateTime start, LocalDateTime end) {
        return matching.date.between(start, end);
    }

    private LocalDateTime getEndOfDay(LocalDateTime date) {
        return date.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
    }

    private LocalDateTime getStartOfDay(LocalDateTime date) {
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
}
