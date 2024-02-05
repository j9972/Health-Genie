package com.example.healthgenie.boundedContext.ptrecord.repository;

import static com.example.healthgenie.boundedContext.ptrecord.entity.QPtProcess.ptProcess;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PtProcessQueryRepository {

    private final JPAQueryFactory query;

    public List<PtProcess> findAll(String keyword) {
        return query
                .selectFrom(ptProcess)
                .where(ptProcess.content.like("%" + keyword + "%"))
                .orderBy(ptProcess.id.desc())
                .fetch();
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
}
