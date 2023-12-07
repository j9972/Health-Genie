package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.UserUpdateRequest;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/user/role")
    public ResponseEntity<Result> updateRole(@RequestBody UserUpdateRequest request) {
        userService.updateRole(request.getRole());

        return ResponseEntity.ok(Result.of("Role이 업데이트 되었습니다."));
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<Result> updateNickname(@RequestBody UserUpdateRequest request) {
        userService.updateNickname(request.getNickname());

        return ResponseEntity.ok(Result.of("Nickname이 업데이트 되었습니다."));
    }
}