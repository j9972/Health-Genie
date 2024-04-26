package com.example.healthgenie.boundedContext.auth.service.feign;

import com.example.healthgenie.boundedContext.auth.dto.OAuthTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-auth", url = "https://accounts.google.com")
public interface GoogleAuthClient {
    @PostMapping("/o/oauth2/token")
    OAuthTokenResponse getToken(@RequestParam("grant_type") String grantType,
                                @RequestParam("client_id") String clientId,
                                @RequestParam("client_secret") String clientSecret,
                                @RequestParam("redirect_uri") String redirectUri,
                                @RequestParam("code") String code
    );
}
