package com.example.healthgenie.boundedContext.review.repository;

import com.example.healthgenie.boundedContext.review.entity.PtReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtReviewRepository extends JpaRepository<PtReview, Long> {


    PtReview findByMemberIdAndTrainerId(Long userId, Long trainerId);
}
