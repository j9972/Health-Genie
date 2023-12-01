package com.example.healthgenie.boundedContext.ptrecord.repository;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.ptrecord.entity.QPtProcess.ptProcess;

@RequiredArgsConstructor
@Repository
public class PtProcessQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<PtProcess> findAll(String keyword) {
        return queryFactory
                .selectFrom(ptProcess)
                .where(ptProcess.content.like("%" + keyword + "%"))
                .orderBy(ptProcess.id.desc())
                .fetch();
    }
}
