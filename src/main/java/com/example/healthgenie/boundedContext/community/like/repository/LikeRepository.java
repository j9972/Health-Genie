package com.example.healthgenie.boundedContext.community.like.repository;

import com.example.healthgenie.boundedContext.community.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long memberId);
    List<Like> findAllByPostId(Long postId);
}
