package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.boundedContext.chat.entity.dto.MessageRequest;
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

    @MessageMapping("/chat/send")
    public void chat(MessageRequest request) {
        log.info("REQUEST /chat/send, request={}", request);
        chatMessageService.sendMessage(request);
        messagingTemplate.convertAndSend("/topic/chat/" + request.getRoomId(), request);
        // receiverId? roomId?
        // request? messageContent?

        // APIC Testing
        // Request URL : ws://localhost:1234/ws
        // Connection Type : STOMP
        // Subscription URL : /topic/chat/{id}
    }
}
