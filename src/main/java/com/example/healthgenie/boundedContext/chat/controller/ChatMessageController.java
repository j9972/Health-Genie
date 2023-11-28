package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message") // -> /app/chat/message
    public void chat(MessageRequest request) {
        log.info("REQUEST /chat/send, request={}", request);
        chatMessageService.sendMessage(request);
        messagingTemplate.convertAndSend("/topic/chat/room/" + request.getRoomId(), request); // -> /topic/chat/room/{roomId}
    }
}
