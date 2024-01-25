package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.JwtResponse;
import com.example.healthgenie.boundedContext.user.dto.SignInResponse;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("----- AuthController.redirect -----");
        log.info("registrationId={}", registrationId);
        log.info("code={}", code);
        log.info("state={}", state);

        SignInResponse result = authService.redirect(
                TokenRequest.builder()
                        .registrationId(registrationId)
                        .code(code)
                        .state(state)
                        .build()
        );

        JwtResponse jwt = JwtResponse.builder()
                .userId(result.getUserId())
                .role(result.getRole())
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .build();

        return ResponseEntity.ok(Result.of(jwt));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Result> refreshToken(@RequestBody TokenRequest tokenRequest) {
        log.info("----- AuthController refreshToken -----");
        log.info("TokenRequest.registrationId={}", tokenRequest.getRegistrationId());
        log.info("TokenRequest.refreshToken={}", tokenRequest.getRefreshToken());

        SignInResponse response = authService.refreshToken(tokenRequest);

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping("/logout/oauth2/code/{registrationId}")
    public void logout(@PathVariable String registrationId,
                       HttpServletRequest request,
                       HttpServletResponse response
    ) {
        /*
        TODO : 카카오/구글과 함께 로그아웃 기능 구현
               현재는 자체 서비스 로그아웃만 구현됨
         */
        authService.logout(request, response);
    }
}