package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatMessageRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.ChatErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.ChatErrorResult.ROOM_NOT_FOUND;
import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(MessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        User sender = userRepository.findByEmail(request.getSenderEmail())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        ChatMessage message = ChatMessage.builder()
                .messageContent(request.getContent())
                .chatRoom(chatRoom)
                .sender(sender)
                .build();

        chatMessageRepository.save(message);
    }

    public List<MessageResponse> getMessages(Long roomId, int page, int size) {
        User currentUser = SecurityUtils.getCurrentUser();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        if(!isRelated(currentUser, chatRoom)) {
            throw new ChatException(NO_PERMISSION);
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedDateDesc(roomId, pageable);

        return MessageResponse.of(messages.getContent());
    }

    private boolean isRelated(User user, ChatRoom room) {
        return Objects.equals(user.getId(), room.getSender().getId()) || Objects.equals(user.getId(), room.getReceiver().getId());
    }
}
