package com.example.healthgenie.controller;

import com.example.healthgenie.domain.user.dto.TestSignUpRequest;
import com.example.healthgenie.domain.user.dto.TestSignUpResponse;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/update/{userId}")
    public ResponseEntity<String> updateRole(@PathVariable Long userId, @RequestParam Role role) {
        userService.updateRole(userId, role);

        return ResponseEntity.ok("update");
    }

    /**
     * 테스트용 엔드포인트
     */
    @PostMapping("/test/create")
    public ResponseEntity<TestSignUpResponse> createUser(@RequestBody TestSignUpRequest signUpRequest){
        return ResponseEntity.ok(userService.createUser(signUpRequest));
    }
}
