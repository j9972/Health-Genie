package com.example.healthgenie.boundedContext.trainer.repository;

import static com.example.healthgenie.boundedContext.trainer.entity.QTrainerInfo.trainerInfo;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerQueryRepository {
    private final JPAQueryFactory query;

    public List<TrainerInfo> findAllProfiles(int page, int size) {
        return query
                .selectFrom(trainerInfo)
                .orderBy(trainerInfo.id.desc())
                .offset(page)
                .limit(size)
                .fetch();
    }
}
