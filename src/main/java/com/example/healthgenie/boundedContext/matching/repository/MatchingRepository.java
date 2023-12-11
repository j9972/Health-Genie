package com.example.healthgenie.boundedContext.matching.repository;

import com.example.healthgenie.boundedContext.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByMemberIdAndDate(Long memberId, LocalDateTime date); // 삭제 예정

    Optional<Matching> findByMemberNicknameAndTrainerNickname(String memberNickname, String trainerNickname);

    Optional<Matching> findByDateAndMemberId(LocalDateTime dateTime, Long memberId);

    Optional<Matching> findByDateAndTrainerId(LocalDateTime dateTime, Long trainerId);
}
