package com.example.healthgenie.boundedContext.ptrecord.repository;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtProcessRepository extends JpaRepository<PtProcess, Long> {

    public List<PtProcess> getAllByTrainer(User user);

    @Query("select R from PtProcess R where R.id =:Id")
    public PtProcess findTrainerById(Long Id);

    Page<PtProcess> findAllByTrainerId(Long trainerId, Pageable pageable);

    Page<PtProcess> findAllByMemberId(Long userId, Pageable pageable);
}
