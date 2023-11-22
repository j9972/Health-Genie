package com.example.healthgenie.boundedContext.community.repository;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost,Long> {
}
