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

import static com.example.healthgenie.boundedContext.user.entity.AuthProvider.KAKAO;

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
    public SignInResponse redirect(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest.getCode());

        KakaoUserInfo kakaoUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElse(null);

        // 회원 가입이 안되어있는 경우(최초 로그인 시)
        if(user == null) {
            user = UserResponse.toEntity(userService.signUp(kakaoUserInfo.getEmail(), kakaoUserInfo.getName(), KAKAO));

            Token token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            RefreshToken refreshToken = RefreshToken.builder().keyEmail(user.getEmail()).refreshToken(token.getRefreshToken()).build();

            refreshTokenRepository.save(refreshToken);

            return SignInResponse.builder()
                    .authProvider(KAKAO)
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .userId(user.getId())
                    .role(user.getRole())
                    .build();
        } else {
            RefreshToken refreshToken = refreshTokenRepository.findByKeyEmail(user.getEmail()).get();

            Token token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            return SignInResponse.builder()
                    .authProvider(KAKAO)
                    .accessToken(token.getAccessToken())
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(user.getId())
                    .role(user.getRole())
                    .build();
        }
    }

    private TokenResponse getToken(String authorizationCode) {
        return kakaoTokenClient.getToken(GRANT_TYPE, CLIENT_ID, REDIRECT_URI, authorizationCode);
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoInfoClient.getUserInfo("Bearer " + accessToken);
    }
}