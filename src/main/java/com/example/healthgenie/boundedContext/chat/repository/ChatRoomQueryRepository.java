//package com.example.healthgenie.boundedContext.chat.repository;
//
//import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
//import com.example.healthgenie.boundedContext.user.entity.User;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//import static com.example.healthgenie.boundedContext.chat.entity.QChatRoom.chatRoom;
//
//@Repository
//@RequiredArgsConstructor
//public class ChatRoomQueryRepository {
//
//    private final JPAQueryFactory query;
//
//    public Optional<ChatRoom> findChatRoomByUsers(User user1, User user2) {
//        return Optional.ofNullable(
//                query
//                        .selectFrom(chatRoom)
//                        .where(
//                                senderEq(user1).and(receiverEq(user2))
//                                        .or(
//                                                senderEq(user2).and(receiverEq(user1))
//                                        )
//                        )
//                        .fetchOne()
//        );
//    }
//
//    private BooleanExpression receiverEq(User user2) {
//        return chatRoom.receiver.eq(user2);
//    }
//
//    private BooleanExpression senderEq(User user1) {
//        return chatRoom.sender.eq(user1);
//    }
//}
