package com.example.healthgenie.boundedContext.community.repository;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.community.entity.QCommunityPost.communityPost;

@RequiredArgsConstructor
@Repository
public class CommunityPostQueryRepository {

    private final JPAQueryFactory queryFactory;

    // TODO : 페이징 or 무한 스크롤 처리
    public List<CommunityPost> findAll(String keyword) {
        return queryFactory
                .selectFrom(communityPost)
                .where(postTitleLike(keyword))
                .orderBy(communityPost.id.desc())
                .fetch();
    }

    private BooleanExpression postTitleLike(String keyword) {
        return communityPost.title.like("%" + keyword + "%");
    }
}
