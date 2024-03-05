package com.example.healthgenie.boundedContext.email.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
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
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final MailService mailService;
    private final RedisService redisService;
    private final UserService userService;

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

            log.info("code : {} ", builder.toString());
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(CommonErrorResult.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public void updateUniv(String univ_name, Long userId) throws IOException {
        User user = userService.findById(userId);
        userService.update(user, UserRequest.builder().uniName(univ_name).build());
    }

    @Transactional
    public void updateUnivVerify(Long userId) throws IOException {
        User user = userService.findById(userId);
        userService.update(user, UserRequest.builder().emailVerify(true).build());
    }
}
