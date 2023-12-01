package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.UpdateRequest;
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

    @PatchMapping("/user")
    public ResponseEntity<Result> updateRole(@RequestBody UpdateRequest request) {
        userService.updateRole(request.getRole());

        return ResponseEntity.ok(Result.of("Role이 업데이트 되었습니다."));
    }
}