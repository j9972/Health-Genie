package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomResponse;
import com.example.healthgenie.boundedContext.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> JoinChatRoom(@RequestBody RoomRequest request) {
        Long roomId = chatRoomService.joinChatRoom(request);

        URI chatRoomUri = UriComponentsBuilder.newInstance()
                .path("/chat/rooms/{roomId}")
                .buildAndExpand(roomId)
                .toUri();

        return ResponseEntity.ok(chatRoomUri);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getChatRooms() {
        return ResponseEntity.ok(chatRoomService.getRoomList());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getChatRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.getRoomDetail(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> removeChatRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.deleteRoom(roomId));
    }
}
