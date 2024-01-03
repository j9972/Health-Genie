package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingUserRepository extends JpaRepository<MatchingUser, Long> {
}
