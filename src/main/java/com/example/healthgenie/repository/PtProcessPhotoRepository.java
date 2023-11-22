package com.example.healthgenie.repository;

import com.example.healthgenie.domain.ptrecord.entity.PtProcessPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtProcessPhotoRepository extends JpaRepository<PtProcessPhoto, Long> {

}
