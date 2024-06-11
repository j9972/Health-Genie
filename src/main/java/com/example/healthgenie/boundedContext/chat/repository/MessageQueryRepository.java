package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.dto.RoomMessageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.chat.entity.QMessage.message;
import static com.example.healthgenie.boundedContext.chat.entity.QRoom.room;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageQueryRepository {

    private final JPAQueryFactory query;

    public List<RoomMessageResponse> getLastMessages(List<Long> roomIds) {
        return query
                .select(Projections.constructor(RoomMessageResponse.class,
                        room.id, message.content.max())
                )
                .from(message)
                .join(room).on(room.id.eq(message.room.id))
                .where(room.id.in(roomIds))
                .groupBy(room.id)
                .fetch();
    }
}
