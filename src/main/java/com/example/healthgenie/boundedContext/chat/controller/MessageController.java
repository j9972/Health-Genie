package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.chat.service.MessageService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/chat/{roomId}")
    public void broadcasting(@DestinationVariable Long roomId, ChatMessageRequest request) {
        MessageResponse response = messageService.save(roomId, request);

        simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, response);
    }

    @GetMapping("/chat/{roomId}/messages")
    public ResponseEntity<Result> findAll(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        List<Message> response = messageService.findAll(roomId, user);

        return ResponseEntity.ok(Result.of(MessageResponse.of(response)));
    }
}
