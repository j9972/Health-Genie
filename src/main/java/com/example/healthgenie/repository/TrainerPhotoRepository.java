package com.example.healthgenie.repository;

import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerPhotoRepository extends JpaRepository<TrainerPhoto,Long> {

    List<TrainerPhoto> findAllByTrainerId(Long Id);
}
