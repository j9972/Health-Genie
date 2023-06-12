package com.example.healthgenie.repository;

import com.example.healthgenie.domain.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost,Long> {
//     List<CommunityPost> findAllOrderByDateDesc(Pageable pageable);

}
