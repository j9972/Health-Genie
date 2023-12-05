package com.example.healthgenie.boundedContext.ptreview.repository;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PtReviewRepository extends JpaRepository<PtReview, Long> {


    PtReview findByMemberNicknameAndTrainerNickname(String userNickname, String trainerNickname);

    Page<PtReview> findAllByTrainerId(Long trainerId, Pageable pageable);

    Page<PtReview> findAllByMemberId(Long userId, Pageable pageable);
}
