package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.dto.KakaoUnlinkResponse;
import com.example.healthgenie.boundedContext.auth.dto.KakaoUserInfo;
import com.example.healthgenie.boundedContext.auth.dto.TokenResponse;
import com.example.healthgenie.boundedContext.auth.service.feign.KakaoAuthClient;
import com.example.healthgenie.boundedContext.auth.service.feign.KakaoInfoClient;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.healthgenie.base.constant.Constants.*;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

@Service
@RequiredArgsConstructor
public class KakaoRequestService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoInfoClient kakaoInfoClient;
    private final JwtUtils jwtUtils;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    public JwtResponse call(String code) {
        TokenResponse tokenResponse = getAccessToken(code);

        KakaoUserInfo kakaoUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> userService.signUp(kakaoUserInfo.getEmail(), kakaoUserInfo.getName(), KAKAO));

        String access = jwtUtils.createJwt("access", user.getEmail(), user.getRole().getCode(), ACCESS_TOKEN_EXPIRATION_MS);
        String refresh = jwtUtils.createJwt("refresh", user.getEmail(), user.getRole().getCode(), REFRESH_TOKEN_EXPIRATION_MS);

        refreshTokenService.save(refresh, user.getEmail(), REFRESH_TOKEN_EXPIRATION_MS);

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

    public TokenResponse getAccessToken(String code) {
        return kakaoAuthClient.getToken(GRANT_TYPE, CLIENT_ID, REDIRECT_URI, code);
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoInfoClient.getUserInfo("Bearer " + accessToken);
    }

    public KakaoUnlinkResponse unlink(String accessToken) {
        return kakaoInfoClient.unlink(BEARER_PREFIX + accessToken);
    }
}
