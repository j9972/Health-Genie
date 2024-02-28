package com.example.healthgenie.boundedContext.community.photo.repository;

import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    void deleteAllByPostId(Long postId);
    List<Photo> findAllByPostId(Long postId);
}
