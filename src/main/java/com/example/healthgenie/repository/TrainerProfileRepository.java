package com.example.healthgenie.repository;

import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerProfileRepository extends JpaRepository<TrainerInfo,Long> {
}
