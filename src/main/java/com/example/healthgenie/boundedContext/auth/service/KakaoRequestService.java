package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.boundedContext.auth.dto.KakaoUserInfo;
import com.example.healthgenie.boundedContext.auth.dto.OAuthTokenResponse;
import com.example.healthgenie.boundedContext.auth.service.feign.KakaoAuthClient;
import com.example.healthgenie.boundedContext.auth.service.feign.KakaoInfoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.healthgenie.base.constant.Constants.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class KakaoRequestService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoInfoClient kakaoInfoClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    public OAuthTokenResponse getAccessToken(String code) {
        return kakaoAuthClient.getToken(GRANT_TYPE, CLIENT_ID, REDIRECT_URI, code);
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoInfoClient.getUserInfo("Bearer " + accessToken);
    }

    public void unlink(String accessToken) {
        kakaoInfoClient.unlink(BEARER_PREFIX + accessToken);
    }
}
