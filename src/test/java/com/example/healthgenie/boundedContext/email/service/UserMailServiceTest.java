package com.example.healthgenie.boundedContext.email.service;

import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserMailServiceTest {

    @Autowired
    TestKrUtils testUtils;

    @Autowired
    UserMailService userMailService;

    @Autowired
    RedisService redisService;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    User user;

    @BeforeEach
    void before() {
        user = testUtils.createUser("test1", Role.EMPTY, "jh485200@gmail.com");
    }

    @Test
    @DisplayName("이메일에 검증 코드 보내기")
    void sendCode() throws MessagingException {
        // given
        testUtils.login(user);

        // when
        String code = userMailService.sendCode(user.getEmail());

        // then
        assertThat(code).isNotEmpty();
    }

    @Test
    @DisplayName("코드 검증하기")
    void verify() throws MessagingException {
        // given
        testUtils.login(user);

        String email = "jh485200@gmail.com";
        String authCode = userMailService.sendCode(user.getEmail());
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        // when

        // then
        assertThat(redisAuthCode.equals(authCode)).isEqualTo(redisService.checkExistsValue(redisAuthCode));
    }
}