package com.example.healthgenie.boundedContext.trainer.profile.repository;

import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<TrainerInfo, Long> {
    Boolean existsByMemberNickname(String nickname);

    Optional<TrainerInfo> findByMemberId(Long userId);

    Optional<TrainerInfo> findByIdAndMemberId(Long id, Long userId);
}
