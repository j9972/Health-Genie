package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.chat.repository.MessageRepository;
import com.example.healthgenie.boundedContext.chat.repository.RoomUserRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.ChatErrorResult.NO_PERMISSION;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final RoomUserRepository roomUserRepository;

    @Transactional
    public MessageResponse save(Long roomId, ChatMessageRequest request) {
        User user = userService.findById(request.getSenderId());
        Room room = roomService.findById(roomId);

        Message savedMessage = messageRepository.save(Message.builder()
                .room(room)
                .sender(user)
                .content(request.getContent())
                .build());

        return MessageResponse.of(savedMessage);
    }

    public List<Message> findAll(Long roomId, User user) {
        RoomUser roomUser = roomUserRepository.findByRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> new ChatException(NO_PERMISSION));

        return messageRepository.findAllByRoomIdOrderByCreatedDateDesc(roomId);
    }
}