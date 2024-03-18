package com.example.healthgenie.boundedContext.ptrecord.repository;

import static com.example.healthgenie.boundedContext.ptrecord.entity.QPtProcess.ptProcess;
import static com.example.healthgenie.boundedContext.ptreview.entity.QPtReview.ptReview;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PtProcessQueryRepository {

    private final JPAQueryFactory query;

    public Slice<PtProcess> findAll(String keyword, Long lastId, Pageable pageable) {
        List<PtProcess> reviews = query
                .selectFrom(ptProcess)
                .where(
                        idLt(lastId),
                        ptProcess.content.like("%" + keyword + "%")

                )
                .orderBy(ptProcess.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return LastPage(pageable, reviews);
    }


    public List<PtProcess> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return query
                .selectFrom(ptProcess)
                // between은 localdate를 지원하지 않으므로, atStartOfDay로 자정이라는 특정 시간으로 초기화
                .where(ptProcess.createdDate.between(searchStartDate.atStartOfDay(),
                        searchEndDate.atStartOfDay().plusDays(1)))
                .orderBy(ptProcess.id.desc())
                .fetch();
    }

    public List<PtProcess> findAllByTrainerId(Long trainerId, int page, int size) {
        return query
                .selectFrom(ptProcess)
                .where(ptProcess.trainer.id.eq(trainerId))
                .orderBy(ptProcess.id.desc())
                .offset(page)
                .limit(size)
                .fetch();
    }

    public List<PtProcess> findAllByMemberId(Long memberId, int page, int size) {
        return query
                .selectFrom(ptProcess)
                .where(ptProcess.member.id.eq(memberId))
                .orderBy(ptProcess.id.desc())
                .offset(page)
                .limit(size)
                .fetch();
    }

    private Slice<PtProcess> LastPage(Pageable pageable, List<PtProcess> process) {
        boolean hasNext = false;

        if (process.size() > pageable.getPageSize()) {
            hasNext = true;
            process.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(process, pageable, hasNext);
    }

    private BooleanExpression idLt(Long processId) {
        if (processId == null) {
            return null;
        }
        return ptReview.id.lt(processId);
    }
}
