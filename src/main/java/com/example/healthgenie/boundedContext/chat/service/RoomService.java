package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.chat.repository.*;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.healthgenie.base.exception.ChatErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.ChatErrorResult.ROOM_NOT_FOUND;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final RoomQueryRepository roomQueryRepository;
    private final MessageRepository messageRepository;
    private final ChatRedisRepository chatRedisRepository;

    @Transactional
    public Long createChatRoom(ChatRoomRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        User anotherUser = userRepository.findById(request.getAnotherUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        int roomHashCode = createRoomHashCode(user, anotherUser);

        Optional<Room> opChatRoom = roomRepository.findByRoomHashCode(roomHashCode);
        if(opChatRoom.isPresent()) {
            Room room = opChatRoom.get();

            if(!room.isActive()){
                activeChatRoom(room);
            }

            activeChatUser(room, anotherUser);
            activeChatUser(room, user);

            return room.getId();
        }

        return createNewRoom(roomHashCode, user, anotherUser).getId();
    }

    public List<ChatRoomResponse> findAll(User user, Long lastId, Pageable pageable) {
        return roomQueryRepository.findAll(user.getId(), lastId, pageable);
    }

    public List<GetMessageResponse> getMessages(Long roomId, User user) {
        List<GetMessageResponse> responses = new ArrayList<>();

        Room room = roomRepository.findByIdAndActive(roomId, true)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        List<RoomUser> roomUsers = room.getRoomUsers();
        for (RoomUser roomUser : roomUsers) {
            if(roomUser.getUser().getId().equals(user.getId())) {
                List<Message> messages = messageRepository.findAllByRoomIdOrderByCreatedDateAsc(roomId);
                for (Message message : messages) {
                    responses.add(GetMessageResponse.of(message));
                }
                return responses;
            }
        }

        throw new ChatException(NO_PERMISSION);
    }

    @Transactional
    public void deleteChatRoom(Long roomId, User user) {
        Room room = roomRepository.findByIdAndActive(roomId, true)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        // 권한 체크
        checkRelation(user, room);

        boolean isAllOut = true;
        List<RoomUser> roomUsers = room.getRoomUsers();
        for (RoomUser roomUser : roomUsers) {
            // 채팅 사용자가 현재 요청한 사용자와 같다면, 비활성화
            if(roomUser.getUser().getId().equals(user.getId())) {
                roomUser.inactive();
            }
            // 채팅 사용자가 활성화 되어 있다면, 해당 채팅방을 모두 비활성화 하지 않은 것
            if(roomUser.isActive()) {
                isAllOut = false;
            }
        }

        if(isAllOut) {
            room.inactive();
        }
    }

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));
    }

    private void checkRelation(User user, Room room) {
        room.getRoomUsers().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .findAny()
                .orElseThrow(() -> new ChatException(NO_PERMISSION));
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
                .active(true)
                .build();

        roomRepository.save(room);

        mapUsersAndRoom(user, anotherUser, room);

        return room;
    }

    private boolean existsRoom(int roomHashCode) {
        return roomRepository.existsByRoomHashCode(roomHashCode);
    }

    private int createRoomHashCode(User user, User anotherUser) {
        Long userId = user.getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }

    private void activeChatRoom(Room room) {
        room.active();
    }

    private void activeChatUser(Room room, User user) {
        room.getRoomUsers().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserException(USER_NOT_FOUND))
                .active();
    }
}