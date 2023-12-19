package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatErrorResult;
import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행되면 대기하고 있던 onMessage가 메시지를 받아 messagingTemplate를 이용하여 websocket 클라이언트들에게 메시지 전달
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("RedisSubscriber.onMessage() 실행");
        log.info("message={}", message);
        try {
            // redis에서 발행된 데이터를 받아 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info("publishMessage={}", publishMessage);

            ChatMessageRequest roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);
            log.info("roomMessage={}", roomMessage);

            GetMessageResponse chatMessageResponse = new GetMessageResponse(roomMessage);
            log.info("chatMessageResponse={}", chatMessageResponse);

            // Websocket 구독자에게 채팅 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/rooms/" + roomMessage.getRoomId(), chatMessageResponse);
        } catch (Exception e) {
            log.info("RedisSubscriber.onMessage() 에러");
            throw new ChatException(ChatErrorResult.NO_PERMISSION);
        }
    }
}
