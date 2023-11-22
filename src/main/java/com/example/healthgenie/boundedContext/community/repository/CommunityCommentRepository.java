package com.example.healthgenie.boundedContext.community.repository;

import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {
}
