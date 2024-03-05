package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.service.MessageService;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations messageSendingOperations;
//    private final ChatMessageService chatMessageService;
    private final MessageService messageService;
    private final UserRepository userRepository;

    // 메세지 생성 및 보내기
//    @MessageMapping("/chat/messages/{roomId}") // -> /pub/chat/messages
//    public void chat(@DestinationVariable Long roomId, MessageRequest request) {
//        log.info("REQUEST /chat/messages/{}, request={}", roomId, request);
//
//        // 메시지 생성
//        chatMessageService.sendMessage(roomId, request);
//
//        // 메시지 보냄(+채팅방 구독)
//        messageSendingOperations.convertAndSend("/sub/chat/rooms/" + roomId, request); // -> /sub/chat/rooms/{roomId}
//    }

    // 메세지 가져오기
//    @GetMapping("/chat/messages/{roomId}")
//    public ResponseEntity<Result> getMessages(@PathVariable Long roomId,
//                                              @RequestParam(defaultValue = "0") int page,
//                                              @RequestParam(defaultValue = "10") int size) {
//        List<MessageResponse> messages = chatMessageService.getMessages(roomId, page, size);
//
//        return ResponseEntity.ok(Result.of(messages));
//    }

    /**
     * websocket "/pub/chat/enter"로 들어오는 메시징을 처리한다.
     * 채팅방에 입장했을 경우
     */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageRequest request) {
        messageService.enter(request);
    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest request) {
        messageService.sendMessage(request);
    }
}
