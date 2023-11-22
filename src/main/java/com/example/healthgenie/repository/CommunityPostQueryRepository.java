package com.example.healthgenie.repository;

import com.example.healthgenie.domain.community.dto.PostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.domain.community.entity.QCommunityPost.communityPost;

@RequiredArgsConstructor
@Repository
public class CommunityPostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<PostResponse> findAll() {
        return queryFactory
                .select(Projections.fields(PostResponse.class,
                        communityPost.id,
                        communityPost.title,
                        communityPost.content,
                        communityPost.member.id))
                .from(communityPost)
                .fetch();
    }
}
