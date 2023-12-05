package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByMemberNicknameAndTrainerNickname(String memberNickName, String trainerNickName);

    Optional<Matching> findByMemberIdAndDate(Long memberId, LocalDate date); // 삭제 예정
}
