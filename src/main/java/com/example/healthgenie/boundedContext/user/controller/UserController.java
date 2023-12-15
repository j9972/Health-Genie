package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<Result> update(@PathVariable Long userId, @RequestBody UserRequest request) throws IOException {
        UserResponse response = userService.edit(userId, request);

        return ResponseEntity.ok(Result.of(response));
    }
}