package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.example.healthgenie.boundedContext.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

//    private final ChatRoomService chatRoomService;
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Result> getOneToOneChatRooms(
            @RequestBody ChatRoomRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<ChatRoomResponse> responses = roomService.getChatRooms(request, pageable);

        return ResponseEntity.ok(Result.of(responses));
    }

    @PostMapping
    public ResponseEntity<Result> createOneToOneChatRoom(@RequestBody ChatRoomRequest request) {
        Long response = roomService.createChatRoom(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<Result> getMessages(@PathVariable Long roomId, @RequestBody ChatRoomRequest request) {
        List<GetMessageResponse> response = roomService.getMessages(roomId, request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<Result> deleteChatRoom(@PathVariable Long roomId, @RequestBody ChatRoomRequest request) {
        roomService.deleteChatRoom(roomId, request);
        return ResponseEntity.ok(Result.of("채팅방 삭제 성공"));
    }

//    @PostMapping
//    public ResponseEntity<Result> joinChatRoom(@RequestBody RoomRequest request) {
//        RoomResponse response = chatRoomService.joinRoom(request);
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @GetMapping
//    public ResponseEntity<Result> getChatRooms() {
//        List<RoomResponse> response = chatRoomService.getRooms();
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @GetMapping("/{roomId}")
//    public ResponseEntity<Result> getChatRoom(@PathVariable Long roomId) {
//        RoomResponse response = chatRoomService.getRoomDetail(roomId);
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @DeleteMapping("/{roomId}")
//    public ResponseEntity<Result> removeChatRoom(@PathVariable Long roomId) {
//        String response = chatRoomService.deleteRoom(roomId);
//
//        return ResponseEntity.ok(Result.of(response));
//    }
}
