package com.example.healthgenie.boundedContext.trainer.repository;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TrainerProfileRepository extends JpaRepository<TrainerInfo, Long> {
    Optional<TrainerInfo> findByMemberId(Long id);

}
