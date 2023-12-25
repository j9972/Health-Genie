package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomHashCodeAndActive(int roomHashCode, boolean isActive);

    Optional<ChatRoom> findByRoomHashCode(int roomHashCode);

    boolean existsByRoomHashCode(int roomHashCode);

    Optional<ChatRoom> findByIdAndActive(Long roomId, boolean isActive);
}
