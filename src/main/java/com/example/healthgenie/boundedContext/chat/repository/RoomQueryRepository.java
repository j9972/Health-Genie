package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.boundedContext.chat.entity.QRoom.room;
import static com.example.healthgenie.boundedContext.chat.entity.QRoomUser.roomUser;
import static com.example.healthgenie.boundedContext.user.entity.QUser.user;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<RoomQueryResponse> findAll(Long userId, Long lastId, Pageable pageable) {
        List<Long> roomIds = queryFactory
                .select(room.id)
                .from(room)
                .join(roomUser).on(roomUser.room.id.eq(room.id))
                .where(
                        roomIdLt(lastId),
                        roomUser.user.id.eq(userId),
                        roomUser.active.eq(true)
                )
                .orderBy(room.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<RoomQueryResponse> contents = queryFactory
                .select(
                        Projections.constructor(RoomQueryResponse.class,
                                roomUser.room.id.as("roomId"), user.nickname, user.role, user.profilePhoto)
                )
                .from(room)
                .join(roomUser).on(roomUser.room.id.eq(room.id))
                .join(user).on(user.id.eq(roomUser.user.id))
                .where(room.id.in(roomIds), user.id.ne(userId))
                .fetch();

        return checkLastPage(pageable, contents);
    }

    private BooleanExpression roomIdLt(Long lastId) {
        if(Objects.isNull(lastId)) {
            return null;
        }
        return room.id.lt(lastId);
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
