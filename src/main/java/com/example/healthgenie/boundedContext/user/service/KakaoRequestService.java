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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.healthgenie.boundedContext.user.entity.AuthProvider.KAKAO;

@Service
@RequiredArgsConstructor
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

    public SignInResponse redirect(TokenRequest tokenRequest) {
        // 카카오에서 넘겨준 엑세스 토큰
        TokenResponse tokenResponse = getToken(tokenRequest.getCode());
        // 카카오에서 넘겨준 유저 정보
        KakaoUserInfo kakaoUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElse(null);

        // 회원 가입이 안되어있는 경우(최초 로그인 시)
        if(user == null) {
            user = UserResponse.toEntity(userService.signUp(kakaoUserInfo.getEmail(), kakaoUserInfo.getName(), KAKAO));
        }

        // 회원 가입이 되어있는 경우
        // 서버에서 생성한 jwt 토큰
        Token token = jwtTokenProvider.createToken(kakaoUserInfo.getEmail(), user.getRole().getCode());

        // 서버에 해당 이메일로 저장된 리프레시 토큰이 없으면 저장(== 첫 회원가입 시 -> 이후에는 리프레시 토큰 검증을 통해 재발급 및 저장함)
        if(!refreshTokenRepository.existsByKeyEmail(user.getEmail())) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .keyEmail(user.getEmail())
                    .refreshToken(token.getRefreshToken())
                    .build();

            refreshTokenRepository.save(newRefreshToken);
        }

        return SignInResponse.builder()
                .authProvider(KAKAO)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public TokenResponse getToken(String authorizationCode) {
        return kakaoTokenClient.getToken(GRANT_TYPE, CLIENT_ID, REDIRECT_URI, authorizationCode);
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoInfoClient.getUserInfo("Bearer " + accessToken);
    }
}