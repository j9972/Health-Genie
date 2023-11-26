package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.entity.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.repository.ChatMessageRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        ChatMessage message = ChatMessage.builder()
                .messageContent(request.getContent())
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .build();

        chatMessageRepository.save(message);
    }
}
