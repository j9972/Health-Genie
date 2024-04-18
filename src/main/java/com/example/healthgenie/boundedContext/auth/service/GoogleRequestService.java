package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.auth.dto.GoogleUserInfo;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.dto.TokenResponse;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleAuthClient;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleInfoClient;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleUnlinkClient;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.healthgenie.base.constant.Constants.ACCESS_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.base.constant.Constants.REFRESH_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleRequestService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final GoogleAuthClient googleAuthClient;
    private final GoogleInfoClient googleInfoClient;
    private final GoogleUnlinkClient googleUnlinkClient;
    private final JwtUtils jwtUtils;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    public JwtResponse call(String code) {
        TokenResponse tokenResponse = getAccessToken(code);

        GoogleUserInfo googleUserInfo = getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElseGet(() -> userService.signUp(googleUserInfo.getEmail(), googleUserInfo.getName(), GOOGLE));

        String access = jwtUtils.createJwt("access", user.getEmail(), user.getRole().getCode(), ACCESS_TOKEN_EXPIRATION_MS);
        String refresh = jwtUtils.createJwt("refresh", user.getEmail(), user.getRole().getCode(), REFRESH_TOKEN_EXPIRATION_MS);

        refreshTokenService.save(refresh, user.getEmail(), REFRESH_TOKEN_EXPIRATION_MS);

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(access)
                .refreshToken(refresh)
                .oauthAccessToken(tokenResponse.getAccessToken())
                .build();
    }

    public TokenResponse getAccessToken(String code) {
        return googleAuthClient.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        return googleInfoClient.getUserInfo("Bearer " + accessToken);
    }

    public void unlink(String accessToken) {
        googleUnlinkClient.unlink(accessToken);
    }
}
