package com.example.healthgenie.repository;

import com.example.healthgenie.domain.ptreview.entity.UserPtReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserPtReviewRepository extends JpaRepository<UserPtReview, Long> {


    @Query("select R from UserPtReview R where R.id =:Id")
    public UserPtReview findsById(Long Id);
//

    public UserPtReview findByMatchingId(Long id);
}
