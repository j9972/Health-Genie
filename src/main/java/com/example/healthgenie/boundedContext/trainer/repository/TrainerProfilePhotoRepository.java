package com.example.healthgenie.boundedContext.trainer.repository;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerProfilePhotoRepository extends JpaRepository<TrainerPhoto, Long> {

    void deleteAllByInfoId(Long profileId);
}
