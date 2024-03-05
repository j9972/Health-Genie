package com.example.healthgenie.boundedContext.community.comment.repository;

import com.example.healthgenie.boundedContext.community.comment.dto.CommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.community.comment.entity.QComment.comment;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CommentResponse> findDtosAllByPostId(Long postId) {
        return queryFactory
                .select(
                        Projections.constructor(CommentResponse.class, comment.id, comment.createdDate, comment.lastModifiedDate, comment.writer.nickname, comment.content, user.profilePhoto.as("profilePhoto"))
                )
                .from(comment)
                .join(user).on(comment.writer.id.eq(user.id))
                .where(comment.post.id.eq(postId))
                .orderBy(comment.id.desc())
                .fetch();
    }
}
