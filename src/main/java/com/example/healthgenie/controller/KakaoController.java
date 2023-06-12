package com.example.healthgenie.controller;

import com.example.healthgenie.dto.RetKakaoOAuth;
import com.example.healthgenie.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/auth/kakao")
public class KakaoController {

    private final Environment env;
    private final KakaoService kakaoService;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    @Value("${social.kakao.url.base}")
    private String baseUrl;
    @GetMapping("/login")
    public String socialLogin() {

        StringBuilder loginUri = new StringBuilder()
                .append(env.getProperty("social.kakao.url.login"))
                .append("?response_type=code")
                .append("&client_id=").append(kakaoClientId)
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectUri);
        log.info(loginUri);
        return loginUri.toString();
    }

    @GetMapping(value = "/redirect")
    public RetKakaoOAuth redirectKakao(@RequestParam String code) {
        return kakaoService.getKakaoTokenInfo(code);
    }
}