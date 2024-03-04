package com.example.healthgenie.boundedContext.chat.repository;

import com.example.healthgenie.boundedContext.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomHashCodeAndActive(int roomHashCode, boolean isActive);

    Optional<Room> findByRoomHashCode(int roomHashCode);

    boolean existsByRoomHashCode(int roomHashCode);

    Optional<Room> findByIdAndActive(Long roomId, boolean isActive);
}
