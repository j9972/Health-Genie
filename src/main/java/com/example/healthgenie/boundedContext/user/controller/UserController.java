package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.exception.UserErrorResult;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.DietResponse;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
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
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<Result> update(@AuthenticationPrincipal User user,
                                         @PathVariable Long userId,
                                         @RequestBody @Valid UserRequest request) {
        if(!Objects.equals(user.getId(), userId)) {
            throw new UserException(UserErrorResult.NO_PERMISSION);
        }

        UserResponse response = userService.edit(userId, request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{userId}/photo")
    public ResponseEntity<Result> update(@AuthenticationPrincipal User user,
                                         @PathVariable Long userId,
                                         @RequestPart MultipartFile photo) throws IOException {
        if(!Objects.equals(user.getId(), userId)) {
            throw new UserException(UserErrorResult.NO_PERMISSION);
        }

        UserResponse response = userService.edit(userId, photo);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{userId}/calculator")
    public ResponseEntity<Result> calculate(@PathVariable Long userId, @RequestParam Integer type) {
        DietResponse response = userService.calculate(userId, type);

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping("/admin")
    public ResponseEntity<Result> admin() {
        UserResponse response = UserResponse.of(userService.signUp("admin@admin.com", "admin", AuthProvider.EMPTY, Role.ADMIN));

        return ResponseEntity.ok(Result.of(response));
    }
}