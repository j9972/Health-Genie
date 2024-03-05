package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.DietResponse;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PatchMapping
    public ResponseEntity<Result> update(@AuthenticationPrincipal User user, @RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.of(userService.update(user, request));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/photo")
    public ResponseEntity<Result> update(@AuthenticationPrincipal User user, @RequestPart MultipartFile photo) throws IOException {
        UserResponse response = UserResponse.of(userService.update(user, photo));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/calculator")
    public ResponseEntity<Result> calculate(@AuthenticationPrincipal User user, @RequestParam Integer type) {
        DietResponse response = userService.calculate(user, type);

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping("/admin")
    public ResponseEntity<Result> admin() {
        UserResponse response = UserResponse.of(userService.signUp("admin@admin.com", "admin", AuthProvider.EMPTY, Role.ADMIN));

        return ResponseEntity.ok(Result.of(response));
    }
}