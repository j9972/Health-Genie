package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
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

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final RoomUserRepository roomUserRepository;

    @Transactional
    public Message save(Long roomId, MessageRequest request) {
        User user = userService.findById(request.getSenderId());
        Room room = roomService.findById(roomId);

        return messageRepository.save(Message.builder()
                .room(room)
                .sender(user)
                .content(request.getContent())
                .build());
    }

    public List<Message> findAll(Long roomId, User user) {
        RoomUser roomUser = roomUserRepository.findByRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> CustomException.NO_PERMISSION);

        return messageRepository.findAllByRoomIdOrderByCreatedDateDesc(roomId);
    }
}