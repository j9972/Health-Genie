package com.example.healthgenie.boundedContext.trainer.photo.repository;

import static com.example.healthgenie.boundedContext.trainer.photo.entity.QTrainerPhoto.trainerPhoto;

import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.entity.enums.PurposeOfUsing;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerProfileQueryPhotoRepository {

    private final JPAQueryFactory query;

    public TrainerPhoto findByPurpose(PurposeOfUsing purpose, Long userId) {
        return query
                .selectFrom(trainerPhoto)
                .where(
                        trainerPhoto.info.id.eq(userId),
                        trainerPhoto.purpose.eq(purpose)
                )
                .fetchOne();
    }
}
