package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.healthgenie.boundedContext.matching.entity.QMatchingUser.matchingUser;

public interface MatchingUserRepository extends JpaRepository<MatchingUser, Long> {
    Optional<MatchingUser> findByUserId(Long userId);
    List<MatchingUser> findAllByUserId(Long userId);
}
