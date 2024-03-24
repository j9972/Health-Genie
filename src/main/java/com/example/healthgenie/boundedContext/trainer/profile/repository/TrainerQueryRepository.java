package com.example.healthgenie.boundedContext.trainer.profile.repository;

import static com.example.healthgenie.boundedContext.trainer.entity.QTrainerInfo.trainerInfo;

import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    public Slice<TrainerInfo> findAll(String name, Long lastId, Pageable pageable) {
        List<TrainerInfo> profiles = query
                .selectFrom(trainerInfo)
                .where(
                        idLt(lastId),
                        trainerInfo.name.like("%" + name + "%")

                )
                .orderBy(trainerInfo.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return LastPage(pageable, profiles);
    }

    public Optional<Long> findMaxId() {
        return Optional.ofNullable(query
                .select(trainerInfo.id.max())
                .from(trainerInfo)
                .fetchOne());
    }

    private Slice<TrainerInfo> LastPage(Pageable pageable, List<TrainerInfo> profile) {
        boolean hasNext = false;

        if (profile.size() > pageable.getPageSize()) {
            hasNext = true;
            profile.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(profile, pageable, hasNext);
    }

    private BooleanExpression idLt(Long profileId) {
        if (profileId == null) {
            return null;
        }
        return trainerInfo.id.lt(profileId);
    }
}
