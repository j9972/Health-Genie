package com.example.healthgenie.boundedContext.refreshtoken.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.boundedContext.refreshtoken.dto.TokenResponse;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refresh")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping
    public ResponseEntity<Result> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = CookieUtils.getCookie(request, "refresh").getValue();

        TokenResponse tokens = refreshTokenService.reissue(refresh);

        return ResponseEntity.ok(Result.of(tokens));
    }
}
