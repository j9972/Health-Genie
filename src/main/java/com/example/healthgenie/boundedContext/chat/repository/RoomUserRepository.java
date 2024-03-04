package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    Page<RoomUser> findAllByUserIdAndActive(Long id, boolean active, Pageable pageable);

    Optional<RoomUser> findByRoomIdAndUserId(Long roomId, Long userId);

    List<RoomUser> findByRoomId(Long roomId);
}
