package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.utils.JwtTokenProvider;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import com.example.healthgenie.boundedContext.user.dto.*;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.feign.GoogleInfoClient;
import com.example.healthgenie.boundedContext.user.service.feign.GoogleTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.boundedContext.user.entity.AuthProvider.GOOGLE;

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
    public SignInResponse redirect(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest);

        GoogleUserInfo googleUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(googleUserInfo.getEmail()).orElse(null);

        // 회원 가입이 안되어있는 경우(최초 로그인 시)
        if(user == null) {
            user = UserResponse.toEntity(userService.signUp(googleUserInfo.getEmail(), googleUserInfo.getName(), GOOGLE));

            Token token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            RefreshToken refreshToken = RefreshToken.builder().keyEmail(user.getEmail()).refreshToken(token.getRefreshToken()).build();

            refreshTokenRepository.save(refreshToken);

            return SignInResponse.builder()
                    .authProvider(GOOGLE)
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .userId(user.getId())
                    .role(user.getRole())
                    .build();
        } else {
            RefreshToken refreshToken = refreshTokenRepository.findByKeyEmail(user.getEmail()).get();

            Token token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            return SignInResponse.builder()
                    .authProvider(GOOGLE)
                    .accessToken(token.getAccessToken())
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(user.getId())
                    .role(user.getRole())
                    .build();
        }
    }

    private TokenResponse getToken(TokenRequest tokenRequest) {
        return googleTokenClient.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, tokenRequest.getCode());
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        return googleInfoClient.getUserInfo("Bearer " + accessToken);
    }
}