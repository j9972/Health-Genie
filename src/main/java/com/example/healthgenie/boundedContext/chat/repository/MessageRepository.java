package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomId(Long roomId);
    Page<Message> findAllByRoomIdOrderByCreatedDateDesc(Long roomId, Pageable pageable);
    List<Message> findAllByRoomIdOrderByCreatedDateDesc(Long roomId);
    List<Message> findAllByRoomIdOrderByCreatedDateAsc(Long id);
}
