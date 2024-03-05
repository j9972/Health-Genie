package com.example.healthgenie.boundedContext.chat.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.RoomSliceResponse;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.service.RoomService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Result> findAll(@AuthenticationPrincipal User user,
                                          @RequestParam(required = false) Long lastId,
                                          Pageable pageable) {
        RoomSliceResponse response = RoomSliceResponse.of(roomService.findAll(user, lastId, pageable));

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping
    public ResponseEntity<Result> saveOrFind(@AuthenticationPrincipal User user,
                                             @RequestBody RoomRequest request) {
        Room room = roomService.saveOrActive(user, request);

        RoomResponse response = RoomResponse.of(room);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<Result> inactive(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        /*
        TODO : inactive 채팅방이 검색되는 문제 해결 해야함
               -> RoomQueryRepository.findAll() 쿼리 수정 필요
         */
        roomService.inactive(roomId, user);
        return ResponseEntity.ok(Result.of("채팅방 삭제 성공"));
    }
}
