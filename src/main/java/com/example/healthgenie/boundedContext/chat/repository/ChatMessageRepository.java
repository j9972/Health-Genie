package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(Long roomId);
    Page<ChatMessage> findAllByChatRoomIdOrderByCreatedDateDesc(Long roomId, Pageable pageable);
    List<ChatMessage> findAllByChatRoomIdOrderByCreatedDateAsc(Long id);
}
