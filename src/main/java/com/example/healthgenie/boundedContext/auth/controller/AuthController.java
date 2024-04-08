package com.example.healthgenie.boundedContext.auth.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
