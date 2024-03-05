package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.utils.JwtTokenProvider;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import com.example.healthgenie.boundedContext.user.dto.GoogleUserInfo;
import com.example.healthgenie.boundedContext.user.dto.JwtResponse;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.dto.TokenResponse;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.feign.GoogleInfoClient;
import com.example.healthgenie.boundedContext.user.service.feign.GoogleTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.GOOGLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleRequestService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleTokenClient googleTokenClient;
    private final GoogleInfoClient googleInfoClient;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Transactional
    public JwtResponse getToken(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest.getCode());

        GoogleUserInfo googleUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElse(userService.signUp(googleUserInfo.getEmail(), googleUserInfo.getName(), GOOGLE));

        String at = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole().getCode());
        String rt = jwtTokenProvider.generateRefreshToken(user.getEmail(), user.getRole().getCode());

        RefreshToken refreshToken = refreshTokenRepository.findByKeyEmail(user.getEmail())
                .orElse(
                        refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .keyEmail(user.getEmail())
                                        .refreshToken(rt)
                                        .build()
                        )
                );

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(at)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    private TokenResponse getToken(String authorizationCode) {
        return googleTokenClient.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, authorizationCode);
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        return googleInfoClient.getUserInfo("Bearer " + accessToken);
    }
}