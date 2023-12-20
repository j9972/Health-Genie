package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.ChatRoomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Page<ChatRoomUser> findAllByUserIdAndActive(Long id, boolean active, Pageable pageable);

    ChatRoomUser findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
