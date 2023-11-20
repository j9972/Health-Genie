package com.example.healthgenie.repository;

import com.example.healthgenie.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    public Optional<Matching> findByMemberIdAndTrainerId(Long userId, Long trainerId);
}
