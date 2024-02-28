package com.example.healthgenie.boundedContext.community.comment.repository;

import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByIdDesc(Long postId);
    Long countByPostId(Long postId);
}
