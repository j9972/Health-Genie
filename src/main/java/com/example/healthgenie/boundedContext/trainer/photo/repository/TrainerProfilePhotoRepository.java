package com.example.healthgenie.boundedContext.trainer.photo.repository;

import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerProfilePhotoRepository extends JpaRepository<TrainerPhoto, Long> {

    void deleteByInfoId(Long infoId);

    List<TrainerPhoto> findAllByInfoId(Long infoId);
}
