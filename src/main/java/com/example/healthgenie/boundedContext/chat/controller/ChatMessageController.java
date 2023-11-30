package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    // 메세지 생성 및 보내기
    @MessageMapping("/chat/messages/{roomId}") // -> /pub/chat/message
    public void chat(@DestinationVariable Long roomId, MessageRequest request) {
        log.info("REQUEST /chat/messages/{}, request={}", roomId, request);

        // 메시지 생성
        chatMessageService.sendMessage(request);

        // 메시지 보냄(+채팅방 구독)
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + roomId, request); // -> /sub/chat/rooms/{roomId}
    }

    // 메세지 가져오기
    @GetMapping("/chat/messages/{roomId}")
    public ResponseEntity<Result> getMessages(@PathVariable Long roomId,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        List<MessageResponse> messages = chatMessageService.getMessages(roomId, page, size);

        return ResponseEntity.ok(Result.of(messages));
    }
}
