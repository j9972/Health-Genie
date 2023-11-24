package com.example.healthgenie.boundedContext.community.repository;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.community.entity.QCommunityPost.communityPost;

@RequiredArgsConstructor
@Repository
public class CommunityPostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CommunityPost> findAll(String keyword) {
        return queryFactory
                .selectFrom(communityPost)
                .where(communityPost.title.like("%" + keyword + "%"))
                .orderBy(communityPost.id.desc())
                .fetch();
    }
}
