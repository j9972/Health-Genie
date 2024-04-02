package com.example.healthgenie.boundedContext.review.repository;

import static com.example.healthgenie.boundedContext.review.entity.QPtReview.ptReview;

import com.example.healthgenie.boundedContext.review.entity.PtReview;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PtReviewQueryRepository {

    private final JPAQueryFactory query;

    // query 사용법 -> join 편리 & 동적 쿼리 사용 가능 & null 자동 제거
    public Slice<PtReview> findAll(String keyword, Long lastId, Pageable pageable) {
        List<PtReview> reviews = query
                .selectFrom(ptReview)
                .where(
                        idLt(lastId),
                        ptReview.content.like("%" + keyword + "%")

                )
                .orderBy(ptReview.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return LastPage(pageable, reviews);
    }

    public List<PtReview> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return query
                .selectFrom(ptReview)
                // between은 localdate를 지원하지 않으므로, atStartOfDay로 자정이라는 특정 시간으로 초기화
                .where(ptReview.createdDate.between(searchStartDate.atStartOfDay(),
                        searchEndDate.atStartOfDay().plusDays(1)))
                .orderBy(ptReview.id.desc())
                .fetch();
    }

    public List<PtReview> findAllByMemberId(Long memberId, int page, int size) {
        return query
                .selectFrom(ptReview)
                .where(ptReview.member.id.eq(memberId))
                .orderBy(ptReview.id.desc())
                .offset(page)
                .limit(size)
                .fetch();
    }

    public List<PtReview> findAllByTrainerId(Long trainerId, int page, int size) {
        return query
                .selectFrom(ptReview)
                .where(ptReview.trainer.id.eq(trainerId))
                .orderBy(ptReview.id.desc())
                .offset(page)
                .limit(size)
                .fetch();
    }

    private Slice<PtReview> LastPage(Pageable pageable, List<PtReview> review) {
        boolean hasNext = false;

        if (review.size() > pageable.getPageSize()) {
            hasNext = true;
            review.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(review, pageable, hasNext);
    }

    private BooleanExpression idLt(Long reviewId) {
        if (reviewId == null) {
            return null;
        }
        return ptReview.id.lt(reviewId);
    }
}