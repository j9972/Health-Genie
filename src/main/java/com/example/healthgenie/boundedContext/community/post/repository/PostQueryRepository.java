package com.example.healthgenie.boundedContext.community.post.repository;

import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.community.post.entity.QPost.post;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostResponse findDtoById(Long postId) {
        return queryFactory
                .select(
                        Projections.constructor(PostResponse.class, post.id, post.createdDate, post.lastModifiedDate, post.title, post.content, post.writer.nickname, user.profilePhoto.as("writerPhoto"))
                )
                .from(post)
                .join(user).on(post.writer.id.eq(user.id))
                .where(post.id.eq(postId))
                .fetchOne();
    }

    public Slice<Post> findAll(String keyword, Long userId, Long lastId, Pageable pageable) {
        List<Post> contents = queryFactory
                .selectFrom(post)
                .where(
                        idLt(lastId),
                        titleLike(keyword),
                        writerIdEq(userId)
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, contents);
    }

    private Slice<Post> checkLastPage(Pageable pageable, List<Post> contents) {
        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            hasNext = true;
            contents.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private BooleanExpression titleLike(String keyword) {
        return post.title.like("%" + keyword + "%");
    }

    private BooleanExpression idLt(Long postId) {
        if(postId == null) {
            return null;
        }
        return post.id.lt(postId);
    }

    private BooleanExpression writerIdEq(Long userId) {
        if(userId == null) {
            return null;
        }
        return post.writer.id.eq(userId);
    }
}
