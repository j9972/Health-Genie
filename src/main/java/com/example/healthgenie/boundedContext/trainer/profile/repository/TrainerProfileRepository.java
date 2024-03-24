package com.example.healthgenie.boundedContext.trainer.profile.repository;

import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TrainerProfileRepository extends JpaRepository<TrainerInfo, Long> {
    Optional<TrainerInfo> findByMemberId(Long id);

    Optional<TrainerInfo> findByIdAndMemberId(Long id, Long userId);
}
