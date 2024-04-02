package com.example.healthgenie.boundedContext.process.photo.repository;

import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessPhotoRepository extends JpaRepository<ProcessPhoto, Long> {

    void deleteByProcessId(Long processId);

    List<ProcessPhoto> findAllByProcessId(Long processId);
}