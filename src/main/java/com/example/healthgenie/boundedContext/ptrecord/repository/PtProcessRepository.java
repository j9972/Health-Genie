package com.example.healthgenie.boundedContext.ptrecord.repository;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PtProcessRepository extends JpaRepository<PtProcess, Long> {

    Page<PtProcess> findAllByTrainerId(Long trainerId, Pageable pageable);

    Page<PtProcess> findAllByMemberId(Long userId, Pageable pageable);
}
