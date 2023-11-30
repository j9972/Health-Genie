package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.TestSignUpResponse;
import com.example.healthgenie.boundedContext.user.dto.TestSignUpRequest;
import com.example.healthgenie.boundedContext.user.dto.UpdateRequest;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/user/{id}")
    public ResponseEntity<Result> updateRole(@PathVariable Long id, @RequestBody UpdateRequest request) {
        userService.updateRole(id, request.getRole());

        return ResponseEntity.ok(Result.of("Role이 업데이트 되었습니다."));
    }

    /**
     * 테스트용 엔드포인트
     */
    @PostMapping("/test/create")
    public ResponseEntity<Result> createUser(@RequestBody TestSignUpRequest signUpRequest){
        TestSignUpResponse response = userService.createUser(signUpRequest);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/test/authentication")
    public ResponseEntity<Result> showAuthenticationPrincipal(@AuthenticationPrincipal User user) {
        log.info("[TEST] user={}", user);
        return ResponseEntity.ok(Result.of(user));
    }
}