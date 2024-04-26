package com.example.healthgenie.boundedContext.auth.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.service.AuthService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("=========== 로그인(회원가입) API 실행 ===========");
        log.info("code={}", code);
        log.info("state={}", state);
        JwtResponse response = authService.getJwt(registrationId, code, state);
        log.info("=========== 로그인(회원가입) API 종료 ===========");

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Result> withdraw(@AuthenticationPrincipal User user, HttpServletRequest request) {
        String oAuthAccessToken = request.getHeader("OAuthAccessToken");
        log.info("OAuthAccessToken={}", oAuthAccessToken);
        log.info("Oauthaccesstoken={}", request.getHeader("Oauthaccesstoken"));

        authService.withdraw(user, oAuthAccessToken);

        return ResponseEntity.ok(Result.of(user.getId() + "_" + user.getAuthProvider() + "_" + user.getNickname() + " 탈퇴 완료"));
    }
}
