package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.chat.entity.QRoomUser.roomUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<RoomQueryResponse> findAll(Long userId, Long lastId, Pageable pageable) {
        List<RoomQueryResponse> contents = queryFactory
                .select(
                        Projections.constructor(RoomQueryResponse.class, roomUser.room.id.as("roomId"), user.nickname, user.role, user.profilePhoto)
                )
                .from(roomUser)
                .join(user).on(user.id.eq(roomUser.user.id))
                .where(
                        roomIdLt(lastId),
                        userIdNe(userId),
                        roomUserActiveEq(true)
                )
                .orderBy(roomUser.room.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, contents);
    }

    private BooleanExpression roomUserActiveEq(boolean active) {
        return roomUser.active.eq(active);
    }

    private BooleanExpression userIdNe(Long userId) {
        return user.id.ne(userId);
    }

    private BooleanExpression roomIdLt(Long roomId) {
        if(roomId == null) {
            return null;
        }
        return roomUser.room.id.lt(roomId);
    }

    private Slice<RoomQueryResponse> checkLastPage(Pageable pageable, List<RoomQueryResponse> contents) {
        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            hasNext = true;
            contents.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

}
