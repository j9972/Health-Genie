package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.healthgenie.boundedContext.chat.entity.QChatMessage.chatMessage;
import static com.example.healthgenie.boundedContext.chat.entity.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class ChatMessageQueryRepository {

    private final JPAQueryFactory query;

    public List<ChatMessage> pagingToRoomId(Long roomId, int page, int size) {
        query
                .selectFrom(chatMessage)
                .where(chatMessage.chatRoom.id.eq(roomId))
                .leftJoin(chatRoom).on(chatRoom.id.eq(chatMessage.chatRoom.id))
                .fetchJoin();
        return null;
    }
}
