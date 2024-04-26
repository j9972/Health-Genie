package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.boundedContext.auth.dto.GoogleUserInfo;
import com.example.healthgenie.boundedContext.auth.dto.OAuthTokenResponse;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleAuthClient;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleInfoClient;
import com.example.healthgenie.boundedContext.auth.service.feign.GoogleUnlinkClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleRequestService {

    private final GoogleAuthClient googleAuthClient;
    private final GoogleInfoClient googleInfoClient;
    private final GoogleUnlinkClient googleUnlinkClient;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    public OAuthTokenResponse getAccessToken(String code) {
        return googleAuthClient.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        return googleInfoClient.getUserInfo("Bearer " + accessToken);
    }

    public void unlink(String accessToken) {
        googleUnlinkClient.unlink(accessToken);
    }
}
