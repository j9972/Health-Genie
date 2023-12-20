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

        // roomHashCode 만들기
        int roomHashCode = createRoomHashCode(user, anotherUser);

        // 기존의 채팅방이 있을 경우
        if(existsRoom(roomHashCode)) {
            ChatRoom chatRoom = chatRoomRepository.findByRoomHashCodeAndActive(roomHashCode, true).orElse(null);

            // 기존의 채팅방이 활성화 되어 있을 경우
            if(chatRoom == null){
                // 기존의 채팅방이 비활성화 되어 있을 경우
                chatRoom = chatRoomRepository.findByRoomHashCode(roomHashCode)
                        .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

                // 기존 채팅방 활성화
                activeChatRoom(chatRoom);

                // 상대방 활성화
                activeChatUser(chatRoom, anotherUser);
            }
            // 사용자 활성화
            activeChatUser(chatRoom, user);

            return chatRoom.getId();
        }

        // 기존의 채팅방이 없는 경우
        return createNewRoom(roomHashCode, user, anotherUser).getId();
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

    public List<ChatRoomResponse> getChatRooms(ChatRoomRequest request, Pageable pageable) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

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
        // TODO : 삭제 권한 체크 필요 - 다른 메서드들도 권한 체크 로직 필요

        ChatRoom chatRoom = chatRoomRepository.findByIdAndActive(roomId, true)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

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

        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomAnotherUser);
    }

    private ChatRoom createNewRoom(int roomHashCode, User user, User anotherUser) {
        ChatRoom newRoom = ChatRoom.builder()
                .roomHashCode(roomHashCode)
                .active(true)
                .build();

        chatRoomRepository.save(newRoom);

        mapUsersAndRoom(user, anotherUser, newRoom);

        return newRoom;
    }

    private void saveIfAlone(ChatRoom chatRoom, User user, User anotherUser) {
        List<ChatRoomUser> chatRoomUsers = chatRoom.getChatRoomUsers();
        if(chatRoomUsers.size() == 1) {
            ChatRoomUser chatRoomUser;
            // 나만 있을 때
            if(chatRoomUsers.get(0).getUser().getId().equals(user.getId())) {
                chatRoomUser = ChatRoomUser.builder()
                        .user(anotherUser)
                        .chatRoom(chatRoom)
                        .build();
            } else {
                // 상대만 있을 때
                chatRoomUser = ChatRoomUser.builder()
                        .user(user)
                        .chatRoom(chatRoom)
                        .build();
            }
            chatRoomUserRepository.save(chatRoomUser);
        }
    }

    private boolean existsRoom(int roomHashCode) {
        return chatRoomRepository.existsByRoomHashCode(roomHashCode);
    }

    private int createRoomHashCode(User user, User anotherUser) {
        Long userId = user.getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }
}