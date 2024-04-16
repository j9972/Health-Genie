package com.example.healthgenie.boundedContext.auth.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.service.AuthService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<Result> redirect(
            @PathVariable("registrationId") String registrationId,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        JwtResponse response = authService.redirect(registrationId, code, state);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Result> withdraw(@AuthenticationPrincipal User user, @RequestParam String code) {
        Long withdraw = authService.withdraw(user, code);

        return ResponseEntity.ok(Result.of(withdraw));
    }
}
