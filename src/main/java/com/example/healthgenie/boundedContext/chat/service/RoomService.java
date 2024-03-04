package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.chat.repository.RoomQueryRepository;
import com.example.healthgenie.boundedContext.chat.repository.RoomRepository;
import com.example.healthgenie.boundedContext.chat.repository.RoomUserRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.ChatErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.ChatErrorResult.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final RoomQueryRepository roomQueryRepository;

    @Transactional
    public Room saveOrActive(User user, RoomRequest request) {
        User anotherUser = userService.findById(request.getAnotherUserId());

        int roomHashCode = createRoomHashCode(user, anotherUser);

        Room room = roomRepository.findByRoomHashCode(roomHashCode)
                .orElse(createNewRoom(roomHashCode, user, anotherUser));

        RoomUser roomUser = roomUserRepository.findByRoomIdAndUserId(room.getId(), user.getId())
                .orElseThrow(() -> new ChatException(NO_PERMISSION));

        roomUser.active();

        return room;
    }

    public List<RoomQueryResponse> findAll(User user, Long lastId, Pageable pageable) {
        return roomQueryRepository.findAll(user.getId(), lastId, pageable);
    }

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));
    }

    @Transactional
    public void deleteChatRoom(Long roomId, User user) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        RoomUser roomUser = roomUserRepository.findByRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> new ChatException(NO_PERMISSION));

        roomUser.inactive();
    }

    private void mapUsersAndRoom(User user, User anotherUser, Room room) {
        RoomUser roomUser = RoomUser.builder()
                .user(user)
                .room(room)
                .active(true)
                .build();

        RoomUser chatRoomAnotherUser = RoomUser.builder()
                .user(anotherUser)
                .room(room)
                .active(true)
                .build();

        RoomUser sender = roomUserRepository.save(roomUser);
        RoomUser receiver = roomUserRepository.save(chatRoomAnotherUser);

        room.getRoomUsers().add(sender);
        room.getRoomUsers().add(receiver);
    }

    private Room createNewRoom(int roomHashCode, User user, User anotherUser) {
        Room room = Room.builder()
                .roomHashCode(roomHashCode)
                .roomUsers(new ArrayList<>())
                .build();

        roomRepository.save(room);

        mapUsersAndRoom(user, anotherUser, room);

        return room;
    }

    private int createRoomHashCode(User user, User anotherUser) {
        Long userId = user.getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }
}