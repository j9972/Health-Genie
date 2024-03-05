package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.utils.JwtTokenProvider;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import com.example.healthgenie.boundedContext.user.dto.*;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.feign.KakaoInfoClient;
import com.example.healthgenie.boundedContext.user.service.feign.KakaoTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoRequestService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoInfoClient kakaoInfoClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Transactional
    public JwtResponse getToken(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest.getCode());

        KakaoUserInfo kakaoUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> userService.signUp(kakaoUserInfo.getEmail(), kakaoUserInfo.getName(), KAKAO));

        String at = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole().getCode());
        String rt = jwtTokenProvider.generateRefreshToken(user.getEmail(), user.getRole().getCode());

        RefreshToken refreshToken = refreshTokenRepository.findByKeyEmail(user.getEmail())
                .orElseGet(() -> refreshTokenRepository.save(RefreshToken.builder().keyEmail(user.getEmail()).refreshToken(rt).build()));

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(at)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    private TokenResponse getToken(String authorizationCode) {
        return kakaoTokenClient.getToken(GRANT_TYPE, CLIENT_ID, REDIRECT_URI, authorizationCode);
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoInfoClient.getUserInfo("Bearer " + accessToken);
    }
}