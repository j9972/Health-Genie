package com.example.healthgenie.boundedContext.community.repository;

import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostPhotoRepository extends JpaRepository<CommunityPostPhoto, Long> {

    List<CommunityPostPhoto> findByPostId(Long postId);
    void deleteAllByPostId(Long postId);
}
