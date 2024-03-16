package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.DietResponse;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Result> getInfo(@AuthenticationPrincipal User user) {
        UserResponse response = UserResponse.of(userService.findById(user.getId()));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/info")
    public ResponseEntity<Result> updateInfo(@AuthenticationPrincipal User user, @Valid UserRequest request) {
        UserResponse response = UserResponse.of(
                userService.update(
                        user, request.getPhoto(), request.getNickname(), request.getGender(), request.getBirth(),
                        request.getHeight(), request.getWeight(), request.getMuscleWeight()
                )
        );

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/role")
    public ResponseEntity<Result> updateRole(@AuthenticationPrincipal User user, @RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.of(userService.update(user, request.getRole()));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/level")
    public ResponseEntity<Result> updateLevel(@AuthenticationPrincipal User user, @RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.of(userService.update(user, request.getLevel()));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/univ")
    public ResponseEntity<Result> updateUniv(@AuthenticationPrincipal User user, @RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.of(userService.update(user, request.getUniName(), request.getEmailVerify()));

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