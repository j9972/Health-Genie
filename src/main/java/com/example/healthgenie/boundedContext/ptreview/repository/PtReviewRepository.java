package com.example.healthgenie.boundedContext.ptreview.repository;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtReviewRepository extends JpaRepository<PtReview, Long> {


    PtReview findByMemberIdAndTrainerId(Long userId, Long trainerId);
}
