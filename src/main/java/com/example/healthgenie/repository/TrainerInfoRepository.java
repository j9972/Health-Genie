package com.example.healthgenie.repository;

import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerInfoRepository extends JpaRepository<TrainerInfo,Long> {
    TrainerInfo findByMemberId(Long Id);
}
