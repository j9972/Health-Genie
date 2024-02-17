package com.example.healthgenie.boundedContext.user.service.feign;

import com.example.healthgenie.boundedContext.user.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoToken", url = "https://kauth.kakao.com")
public interface KakaoTokenClient {

    @PostMapping("/oauth/token")
    TokenResponse getToken(@RequestParam("grant_type") String grantType,
                           @RequestParam("client_id") String clientId,
                           @RequestParam("redirect_uri") String redirectUri,
                           @RequestParam("code") String code
    );
}