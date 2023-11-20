package com.example.healthgenie.repository;

import com.example.healthgenie.domain.ptreview.entity.PtReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PtReviewRepository extends JpaRepository<PtReview, Long> {


    public PtReview findByMemberIdAndTrainerId(Long userId, Long trainerId);

    @Query("select R from PtProcess R where R.id =:reviewId")
    public PtReview findMemberIdById(Long reviewId);

    Page<PtReview> findAllByTrainerId(Long trainerId, Pageable pageable);

    Page<PtReview> findAllByMemberId(Long userId, Pageable pageable);
}
