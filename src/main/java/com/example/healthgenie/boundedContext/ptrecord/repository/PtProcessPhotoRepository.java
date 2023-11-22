package com.example.healthgenie.boundedContext.ptrecord.repository;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtProcessPhotoRepository extends JpaRepository<PtProcessPhoto, Long> {

}
