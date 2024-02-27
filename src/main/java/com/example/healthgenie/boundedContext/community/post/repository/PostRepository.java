package com.example.healthgenie.boundedContext.community.post.repository;

import com.example.healthgenie.boundedContext.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
}
