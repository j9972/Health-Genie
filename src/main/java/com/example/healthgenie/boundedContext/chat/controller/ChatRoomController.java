package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.boundedContext.chat.entity.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.dto.RoomResponse;
import com.example.healthgenie.boundedContext.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/room")
    public ResponseEntity<Long> JoinChatRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(chatRoomService.joinChatRoom(request));
    }

    @GetMapping("/chat/room")
    public ResponseEntity<List<RoomResponse>> getChatRoomList(@RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getRoomList(userId));
    }

    @GetMapping("/chat/room/{roomId}")
    public ResponseEntity<RoomResponse> getChatRoomDetail(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.getRoomDetail(roomId));
    }
}
