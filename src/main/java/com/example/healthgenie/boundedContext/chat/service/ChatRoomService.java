package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.entity.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.dto.RoomResponse;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.healthgenie.base.exception.ChatErrorResult.ROOM_NOT_FOUND;
import static com.example.healthgenie.base.exception.ChatErrorResult.SELF_CHAT;
import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long joinChatRoom(RoomRequest request) {
        if(request.getSenderId().equals(request.getReceiverId())) {
            throw new ChatException(SELF_CHAT);
        }

        Optional<ChatRoom> opChatRoom = chatRoomRepository.findBySenderIdAndReceiverId(request.getSenderId(), request.getReceiverId());
        if(opChatRoom.isPresent()) {
            return opChatRoom.get().getId();
        } else {
            User sender = userRepository.findById(request.getSenderId())
                    .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

            User receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

            ChatRoom chatRoom = ChatRoom.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            return chatRoomRepository.save(chatRoom).getId();
        }
    }

    public List<RoomResponse> getRoomList(Long senderId) {
        return chatRoomRepository.findAllBySenderId(senderId).stream()
                .map(RoomResponse::ofExcludeMessages)
                .toList();
    }

    public RoomResponse getRoomDetail(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        return RoomResponse.of(chatRoom);
    }
}
