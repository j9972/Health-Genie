package com.example.healthgenie.repository;

import com.example.healthgenie.domain.ptreview.entity.PtReivew;
import com.example.healthgenie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PtReviewRepository extends JpaRepository<PtReivew, Long> {


//    @Query("select R from PtReivew R where R.id =:Id")
//    public PtReivew findsById(Long Id);

    public List<PtReivew> getAllByTrainerId(Long trainerId);

    public List<PtReivew> getAllByMemberId(Long userId);
}
