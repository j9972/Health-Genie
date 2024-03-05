package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.example.healthgenie.boundedContext.chat.service.RoomService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Result> getOneToOneChatRooms(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        /*
        TODO : Request에서 현재 사용자의 id를 받는 대신 @AuthenticationPrincipal로 대체하기
               또한 다른 도메인도 적용해서 SecurityUtils 클래스 불필요 -> 삭제하기
         */
        List<ChatRoomResponse> responses = roomService.getChatRooms(user, pageable);

        return ResponseEntity.ok(Result.of(responses));
    }

    @PostMapping
    public ResponseEntity<Result> createOneToOneChatRoom(@RequestBody ChatRoomRequest request) {
        Long response = roomService.createChatRoom(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<Result> getMessages(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        List<GetMessageResponse> response = roomService.getMessages(roomId, user);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<Result> deleteChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        roomService.deleteChatRoom(roomId, user);
        return ResponseEntity.ok(Result.of("채팅방 삭제 성공"));
    }
}
