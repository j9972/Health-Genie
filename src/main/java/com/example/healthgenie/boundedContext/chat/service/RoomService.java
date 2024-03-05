package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoomUser;
import com.example.healthgenie.boundedContext.chat.repository.ChatMessageRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRedisRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomUserRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisRepository chatRedisRepository;

    @Transactional
    public Long createChatRoom(ChatRoomRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        User anotherUser = userRepository.findById(request.getAnotherUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        int roomHashCode = createRoomHashCode(user, anotherUser);

        Optional<ChatRoom> opChatRoom = chatRoomRepository.findByRoomHashCode(roomHashCode);
        if(opChatRoom.isPresent()) {
            ChatRoom chatRoom = opChatRoom.get();

            if(!chatRoom.isActive()){
                activeChatRoom(chatRoom);
            }

            activeChatUser(chatRoom, anotherUser);
            activeChatUser(chatRoom, user);

            return chatRoom.getId();
        }

        return createNewRoom(roomHashCode, user, anotherUser).getId();
    }

    public List<ChatRoomResponse> getChatRooms(User user, Pageable pageable) {
        List<ChatRoomResponse> responses = new ArrayList<>();

        Page<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByUserIdAndActive(user.getId(), true, pageable);
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            responses.add(ChatRoomResponse.of(chatRoomUser));
        }

        return responses;
    }

    public List<GetMessageResponse> getMessages(Long roomId, User user) {
        List<GetMessageResponse> responses = new ArrayList<>();

        ChatRoom chatRoom = chatRoomRepository.findByIdAndActive(roomId, true)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        List<ChatRoomUser> chatRoomUsers = chatRoom.getChatRoomUsers();
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            if(chatRoomUser.getUser().getId().equals(user.getId())) {
                List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedDateAsc(roomId);
                for (ChatMessage message : messages) {
                    responses.add(GetMessageResponse.of(message));
                }
                return responses;
            }
        }

        throw new ChatException(NO_PERMISSION);
    }

    @Transactional
    public void deleteChatRoom(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndActive(roomId, true)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        // 권한 체크
        checkRelation(user, chatRoom);

        boolean isAllOut = true;
        List<ChatRoomUser> chatRoomUsers = chatRoom.getChatRoomUsers();
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            // 채팅 사용자가 현재 요청한 사용자와 같다면, 비활성화
            if(chatRoomUser.getUser().getId().equals(user.getId())) {
                chatRoomUser.inactive();
            }
            // 채팅 사용자가 활성화 되어 있다면, 해당 채팅방을 모두 비활성화 하지 않은 것
            if(chatRoomUser.isActive()) {
                isAllOut = false;
            }
        }

        if(isAllOut) {
            chatRoom.inactive();
        }
    }

    private void checkRelation(User user, ChatRoom chatRoom) {
        chatRoom.getChatRoomUsers().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .findAny()
                .orElseThrow(() -> new ChatException(NO_PERMISSION));
    }

    private void mapUsersAndRoom(User user, User anotherUser, ChatRoom room) {
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(user)
                .chatRoom(room)
                .active(true)
                .build();

        ChatRoomUser chatRoomAnotherUser = ChatRoomUser.builder()
                .user(anotherUser)
                .chatRoom(room)
                .active(true)
                .build();

        ChatRoomUser sender = chatRoomUserRepository.save(chatRoomUser);
        ChatRoomUser receiver = chatRoomUserRepository.save(chatRoomAnotherUser);

        room.getChatRoomUsers().add(sender);
        room.getChatRoomUsers().add(receiver);
    }

    private ChatRoom createNewRoom(int roomHashCode, User user, User anotherUser) {
        ChatRoom room = ChatRoom.builder()
                .roomHashCode(roomHashCode)
                .chatRoomUsers(new ArrayList<>())
                .active(true)
                .build();

        chatRoomRepository.save(room);

        mapUsersAndRoom(user, anotherUser, room);

        return room;
    }

    private boolean existsRoom(int roomHashCode) {
        return chatRoomRepository.existsByRoomHashCode(roomHashCode);
    }

    private int createRoomHashCode(User user, User anotherUser) {
        Long userId = user.getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }

    private void activeChatRoom(ChatRoom chatRoom) {
        chatRoom.active();
    }

    private void activeChatUser(ChatRoom chatRoom, User user) {
        chatRoom.getChatRoomUsers().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserException(USER_NOT_FOUND))
                .active();
    }
}