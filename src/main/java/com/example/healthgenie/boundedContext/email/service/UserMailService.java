package com.example.healthgenie.boundedContext.email.service;



import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional
    public String sendCode(String toEmail) throws MessagingException {

        String title = "Health Genie 이메일 인증 번호";
        String authCode = this.createCode();
//
//        JsonObject jsonObject = new Gson().fromJson(toEmail, JsonObject.class);
//        String email = jsonObject.get("email").getAsString();

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

    public boolean verify(String email, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        return redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }
}
