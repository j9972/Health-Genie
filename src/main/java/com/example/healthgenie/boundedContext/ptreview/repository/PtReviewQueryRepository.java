package com.example.healthgenie.boundedContext.ptreview.repository;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.entity.QPtReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.ptreview.entity.QPtReview.ptReview;

/*
    test 용 query file
 */
@Repository
@RequiredArgsConstructor
public class PtReviewQueryRepository {

    private final JPAQueryFactory query;
    // query 사용법 -> join 편리 & 동적 쿼리 사용 가능 & null 자동 제거
    public List<PtReview> findAll() {
        return query
                .select(ptReview)
                .from(ptReview)
                .where()
                .fetch();
    }
}
