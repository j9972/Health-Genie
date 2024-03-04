package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.chat.entity.QChatRoomUser.chatRoomUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChatRoomResponse> findAll(Long userId, Long lastId, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(ChatRoomResponse.class, chatRoomUser.id.as("roomId"), user.nickname, user.role, user.profilePhoto)
                )
                .from(chatRoomUser)
                .join(user).on(user.id.eq(chatRoomUser.id))
                .where(user.id.ne(userId))
                .fetch();
    }
}
