package com.example.healthgenie.boundedContext.trainer.repository;

import static com.example.healthgenie.boundedContext.trainer.entity.QTrainerInfo.trainerInfo;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerQueryRepository {
    private final JPAQueryFactory query;

    public List<TrainerInfo> findAllProfilesSortByLatest(Long lastIndex) {
        return query
                .selectFrom(trainerInfo)
                .where(trainerInfo.id.loe(lastIndex))
                .orderBy(trainerInfo.id.desc())
                .limit(10)
                .fetch();
    }

    public List<TrainerInfo> findAll(String name) {
        return query
                .selectFrom(trainerInfo)
                .where(trainerInfo.name.like("%" + name + "%"))
                .orderBy(trainerInfo.id.desc())
                .fetch();
    }

    public Optional<Long> findMaxId() {
        return Optional.ofNullable(query
                .select(trainerInfo.id.max())
                .from(trainerInfo)
                .fetchOne());
    }
}
