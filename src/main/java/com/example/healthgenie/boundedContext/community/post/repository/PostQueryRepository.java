package com.example.healthgenie.boundedContext.community.post.repository;

import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.community.post.entity.QPost.post;

@RequiredArgsConstructor
@Repository
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    // TODO : 페이징 or 무한 스크롤 처리
    public List<Post> findAll(String keyword) {
        return queryFactory
                .selectFrom(post)
                .where(postTitleLike(keyword))
                .orderBy(post.id.desc())
                .fetch();
    }

//    public List<FindAllDto> findAll(String keyword) {
//        return queryFactory
//                .select(
//                        Projections.constructor(
//                                FindAllDto.class, post.id, post.title, post.content, post.createdDate, post.writer.nickname, comment.countDistinct().as("commentCount"), like.countDistinct().as("likeCount")
//                        )
//                )
//                .from(post)
//                .leftJoin(comment).on(post.eq(comment.post))
//                .leftJoin(like).on(post.eq(like.post))
//                .where(postTitleLike(keyword))
//                .groupBy(post.id)
//                .fetch();
//    }

    private BooleanExpression postTitleLike(String keyword) {
        return post.title.like("%" + keyword + "%");
    }
}
