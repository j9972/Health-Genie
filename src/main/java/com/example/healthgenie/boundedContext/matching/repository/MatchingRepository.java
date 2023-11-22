package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    public Optional<Matching> findByMemberIdAndTrainerId(Long userId, Long trainerId);
}
