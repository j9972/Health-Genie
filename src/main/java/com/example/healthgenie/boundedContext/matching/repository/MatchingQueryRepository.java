package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
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

    public List<Matching> findAllMatchingsForOneDay(LocalDateTime date, String memberNickname, String trainerNickname) {
        LocalDateTime startOfDay = getStartOfDay(date);
        LocalDateTime endOfDay = getEndOfDay(date);

        return query
                .selectFrom(matching)
                .where(
                        dateBetween(startOfDay, endOfDay),
                        memberNicknameEq(memberNickname),
                        trainerNicknameEq(trainerNickname)
                )
                .orderBy(matching.date.asc())
                .fetch();
    }

    public Matching findOne(LocalDateTime date, String memberNickname, String trainerNickname) {
        return query
                .selectFrom(matching)
                .where(
                        dateEq(date),
                        memberNicknameEq(memberNickname),
                        trainerNicknameEq(trainerNickname)
                )
                .fetchOne();
    }

    private BooleanExpression dateEq(LocalDateTime date) {
        return matching.date.eq(date);
    }

    private BooleanExpression memberNicknameEq(String nickname) {
        return matching.member.nickname.eq(nickname);
    }

    private BooleanExpression trainerNicknameEq(String nickname) {
        return matching.trainer.nickname.eq(nickname);
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
