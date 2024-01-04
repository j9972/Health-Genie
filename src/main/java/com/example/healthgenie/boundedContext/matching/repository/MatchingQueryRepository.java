package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.healthgenie.boundedContext.matching.entity.QMatching.matching;
import static com.example.healthgenie.boundedContext.matching.entity.QMatchingUser.matchingUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class MatchingQueryRepository {

    private final JPAQueryFactory query;

    public List<Matching> findAllByUserIdAndDate(Long userId, LocalDate date) {
        return query
                .selectFrom(matching)
                .join(matching.matchingUsers, matchingUser)
                .where(
                        userIdEq(userId),
                        matchingDateEq(date)
                )
                .orderBy(matchingTimeAsc())
                .fetch();
    }

    private OrderSpecifier<LocalTime> matchingTimeAsc() {
        return matching.time.asc();
    }

    private BooleanExpression matchingDateEq(LocalDate date) {
        return matching.date.eq(date);
    }

    private BooleanExpression userIdEq(Long userId) {
        return user.id.eq(userId);
    }
}
