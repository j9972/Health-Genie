package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.KakaoProfile;
import com.example.healthgenie.domain.user.dto.RetKakaoOAuth;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final Environment env;
    private final Gson gson;
//    @Value("${social.kakao.url.base}")
//    private String baseUrl;

//    @Value("${social.kakao.client-id}")
//    private String kakaoClientId;
//
//    @Value("${social.kakao.redirect}")
//    private String kakaoRedirectUri;

    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);
        String requestUrl = env.getProperty("social.kakao.url.profile");

        if (requestUrl == null) throw new CommonException(CommonErrorResult.BAD_REQUEST);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), KakaoProfile.class);
        }
        throw new CommonException(CommonErrorResult.VALID_OUT);
    }

/*
    public RetKakaoOAuth getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        params.add("grant_type", "authorization_code");
//        params.add("client_id", kakaoClientId);
//        params.add("redirect_uri", baseUrl + kakaoRedirectUri);
        params.add("code", code);

        String requestUri = env.getProperty("social.kakao.url.token");

        // kakao login 되지 않는 경우 [ token 이 없는 경우 ]
        if(requestUri==null){
            throw new CommonException(CommonErrorResult.VALID_OUT);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), RetKakaoOAuth.class);
        }
        throw new CommonException(CommonErrorResult.BAD_REQUEST);
    }

 */

    public void kakaoUnlink(String accessToken) {
        String unlinkUrl = env.getProperty("social.kakao.url.unlink");
        if (unlinkUrl == null) throw new CommonException(CommonErrorResult.ITEM_EMPTY);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) return;
        throw new CommonException(CommonErrorResult.ITEM_EMPTY);
    }
}