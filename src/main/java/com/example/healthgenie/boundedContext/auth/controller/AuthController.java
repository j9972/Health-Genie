package com.example.healthgenie.boundedContext.auth.controller;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.auth.AuthService;
import com.example.healthgenie.boundedContext.refreshtoken.dto.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<Result> getJwt(HttpServletRequest request) {
        if(request.getCookies() == null) {
            throw CustomException.NO_JWT;
        }

        TokenResponse response = authService.getTokens(request.getCookies());

        return ResponseEntity.ok(Result.of(response));
    }
}
