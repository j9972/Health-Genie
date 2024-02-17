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

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
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
        // 구글에서 넘겨준 엑세스 토큰
        TokenResponse tokenResponse = getToken(tokenRequest);
        // 구글에서 넘겨준 유저 정보
        GoogleUserInfo googleUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(googleUserInfo.getEmail()).orElse(null);

        // 회원 가입이 안되어있는 경우(최초 로그인 시)
        if(user == null){
            user = UserResponse.toEntity(userService.signUp(googleUserInfo.getEmail(), googleUserInfo.getName(), GOOGLE));
        }

        // 회원 가입이 되어있는 경우
        // 서버에서 생성한 jwt 토큰
        Token token = jwtTokenProvider.createToken(googleUserInfo.getEmail(), user.getRole().getCode());

        // 서버에 해당 이메일로 저장된 리프레시 토큰이 없으면 저장(== 첫 회원가입 시 -> 이후에는 리프레시 토큰 검증을 통해 재발급 및 저장함)
        if(!refreshTokenRepository.existsByKeyEmail(user.getEmail())) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .keyEmail(user.getEmail())
                    .refreshToken(token.getRefreshToken())
                    .build();

            refreshTokenRepository.save(newRefreshToken);
        }

        return SignInResponse.builder()
                .authProvider(GOOGLE)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public TokenResponse getToken(TokenRequest tokenRequest) {
        return googleTokenClient.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, tokenRequest.getCode());
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        return googleInfoClient.getUserInfo("Bearer " + accessToken);
    }
}