package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.entity.ChatMessage;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatMessageRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRedisRepository;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.base.exception.ChatErrorResult.ROOM_NOT_FOUND;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRedisRepository redisRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅방 입장
    public void enter(ChatMessageRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        // 채팅방에 들어온 정보를 Redis 저장
        redisRepository.userEnterRoomInfo(user.getId(), chatRoom.getId());

        redisRepository.initChatRoomMessageInfo(chatRoom.getId()+"", user.getId());
    }

    //채팅
    @Transactional
    public void sendMessage(ChatMessageRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ChatException(ROOM_NOT_FOUND));

        //채팅 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .messageContent(request.getMessage())
                .build();

        chatMessageRepository.save(chatMessage);

        String topic = channelTopic.getTopic();

        // ChatMessageRequest에 유저정보, 현재시간 저장
        request.setNickName(user.getNickname());
        request.setCreatedDate(chatMessage.getCreatedDate());

        // 일대일 채팅
        redisTemplate.convertAndSend(topic, request);
    }
}