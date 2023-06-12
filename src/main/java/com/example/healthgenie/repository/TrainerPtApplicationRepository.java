package com.example.healthgenie.repository;

import com.example.healthgenie.domain.trainer.entity.TrainerPtApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerPtApplicationRepository extends JpaRepository<TrainerPtApplication,Long> {
}
