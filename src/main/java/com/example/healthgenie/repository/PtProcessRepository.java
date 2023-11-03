package com.example.healthgenie.repository;

import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtProcessRepository extends JpaRepository<PtProcess, Long> {

    @Query("select R from PtProcess R where R.id =:Id")
    public PtProcess findsById(Long Id);

    public List<PtProcess> getAllByTrainer(User user);
}
