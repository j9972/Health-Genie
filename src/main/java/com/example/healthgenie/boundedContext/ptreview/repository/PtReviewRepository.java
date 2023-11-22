package com.example.healthgenie.boundedContext.ptreview.repository;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PtReviewRepository extends JpaRepository<PtReview, Long> {


    public PtReview findByMemberIdAndTrainerId(Long userId, Long trainerId);

    @Query("select R from PtProcess R where R.id =:reviewId")
    public PtReview findMemberIdById(Long reviewId);

    Page<PtReview> findAllByTrainerId(Long trainerId, Pageable pageable);

    Page<PtReview> findAllByMemberId(Long userId, Pageable pageable);
}
