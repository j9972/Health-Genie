package com.example.healthgenie.boundedContext.email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;



@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;
    private final RedisService redisService;
    private final UserService userService;

    private static final OkHttpClient client = new OkHttpClient();
    private static final JSONParser parser = new JSONParser();
    private static final String baseURL = "https://univcert.com/api";


    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    @Transactional
    public String sendCode(String toEmail) throws MailException {

        String title = "Health Genie 이메일 인증 번호";
        String authCode = this.createCode();

        mailService.sendEmail(toEmail, title, authCode);

        redisService.setValues(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
        return authCode;
    }

    private String createCode() {
        int length = 8;
        try {

            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(CommonErrorResult.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public void updateUniv(String univ_name) throws IOException {
        Long userId = SecurityUtils.getCurrentUserId();

        userService.edit(userId, UserRequest.builder().uniName(univ_name).build());
    }

    @Transactional
    public void updateUnivVerify() throws IOException {
        Long userId = SecurityUtils.getCurrentUserId();

        userService.edit(userId, UserRequest.builder().emailVerify(true).build());
    }
}
