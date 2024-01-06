package com.example.healthgenie.boundedContext.ptreview.repository;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.healthgenie.boundedContext.ptreview.entity.QPtReview.ptReview;

@Repository
@RequiredArgsConstructor
public class PtReviewQueryRepository {

    private final JPAQueryFactory query;
    // query 사용법 -> join 편리 & 동적 쿼리 사용 가능 & null 자동 제거
    public List<PtReview> findAll(String keyword) {
        return query
                .selectFrom(ptReview)
                .where(ptReview.content.like("%" + keyword + "%"))
                .orderBy(ptReview.id.desc())
                .fetch();
    }

    public List<PtReview> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return query
                .selectFrom(ptReview)
                // between은 localdate를 지원하지 않으므로, atStartOfDay로 자정이라는 특정 시간으로 초기화
                .where(ptReview.createdDate.between(searchStartDate.atStartOfDay(), searchEndDate.atStartOfDay().plusDays(1)))
                .orderBy(ptReview.id.desc())
                .fetch();
    }

}