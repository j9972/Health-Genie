package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.*;
import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.utils.SecurityUtils;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRequestService implements RequestService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final WebClient webClient;
    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.google.token_uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String USER_INFO_URI;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Override
    public SignInResponse redirect(TokenRequest tokenRequest) {
        // 구글에서 넘겨준 엑세스 토큰
        TokenResponse tokenResponse = getToken(tokenRequest);
        // 구글에서 넘겨준 유저 정보
        GoogleUserInfo googleUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(googleUserInfo.getEmail()).orElse(null);

        // 회원 가입이 안되어있는 경우(최초 로그인 시)
        if(!userRepository.existsByEmail(googleUserInfo.getEmail())){
            UserRegisterDto dto = UserRegisterDto
                    .builder()
                    .email(googleUserInfo.getEmail())
                    .name(googleUserInfo.getName())
                    .role(Role.USER)
                    .authProvider(AuthProvider.GOOGLE)
                    .build();

            user = userService.socialSignUp(dto);
        }

        // 회원 가입이 되어있는 경우
        // 서버에서 생성한 jwt 토큰
        String accessToken = securityUtils.createAccessToken(
                googleUserInfo.getEmail(), AuthProvider.GOOGLE, tokenResponse.getAccessToken());

        String refreshToken = securityUtils.createRefreshToken(
                googleUserInfo.getEmail(), AuthProvider.GOOGLE, tokenResponse.getRefreshToken());

        return SignInResponse.builder()
                .authProvider(AuthProvider.GOOGLE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    @Override
    public TokenResponse getToken(TokenRequest tokenRequest) {
        return webClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("client_id", CLIENT_ID)
                        .with("client_secret", CLIENT_SECRET)
                        .with("code", tokenRequest.getCode())
                        .with("redirect_uri", REDIRECT_URI)
                        .with("grant_type", GRANT_TYPE))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

    }

    @Override
    public GoogleUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(USER_INFO_URI)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
    }

    @Override
    public TokenResponse getRefreshToken(String provider, String refreshToken) {
        return webClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                .fromFormData("client_id", CLIENT_ID)
                        .with("client_secret", CLIENT_SECRET)
                        .with("refresh_token", refreshToken)
                        .with("grant_type", "refresh_token"))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
