package com.example.healthgenie.boundedContext.trainer.repository;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerProfileRepository extends JpaRepository<TrainerInfo,Long> {
}
