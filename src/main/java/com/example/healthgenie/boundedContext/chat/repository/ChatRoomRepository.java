package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<ChatRoom> findAllBySenderId(Long senderId);

    List<ChatRoom> findAllByReceiverId(Long receiver);
}
