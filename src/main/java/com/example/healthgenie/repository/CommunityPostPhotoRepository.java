package com.example.healthgenie.repository;

import com.example.healthgenie.domain.community.entity.CommunityPostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostPhotoRepository extends JpaRepository<CommunityPostPhoto, Long> {
}
