package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.chat.entity.QRoomUser.roomUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<RoomQueryResponse> findAll(Long userId, Long lastId, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(RoomQueryResponse.class, roomUser.id.as("roomId"), user.nickname, user.role, user.profilePhoto)
                )
                .from(roomUser)
                .join(user).on(user.id.eq(roomUser.id))
                .where(user.id.ne(userId), roomUser.active.eq(true))
                .fetch();
    }
}
