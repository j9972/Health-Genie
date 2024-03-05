package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.healthgenie.boundedContext.matching.entity.QMatching.matching;
import static com.example.healthgenie.boundedContext.matching.entity.QMatchingUser.matchingUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class MatchingQueryRepository {

    private final JPAQueryFactory query;

    public List<Matching> findAllByUserIdAndDate(Long userId, LocalDateTime date) {
        return query
                .selectFrom(matching)
                .join(matching.matchingUsers, matchingUser)
                .where(
                        userIdEq(userId),
                        matchingDateBetween(date, date.plusDays(1).minusNanos(1))
                )
                .orderBy(matchingTimeAsc())
                .fetch();
    }

    private OrderSpecifier<LocalDateTime> matchingTimeAsc() {
        return matching.date.asc();
    }

    private BooleanExpression matchingDateBetween(LocalDateTime start, LocalDateTime end) {
        return matching.date.between(start, end);
    }

    private BooleanExpression userIdEq(Long userId) {
        return user.id.eq(userId);
    }
}
