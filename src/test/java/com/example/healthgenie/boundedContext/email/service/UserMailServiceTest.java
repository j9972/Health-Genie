package com.example.healthgenie.boundedContext.email.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;

import com.example.healthgenie.util.TestSyUtils;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserMailServiceTest {

    @Autowired
    TestKrUtils testUtils;

    @Autowired
    UserMailService userMailService;

    @Autowired
    RedisService redisService;

    String AUTH_CODE_PREFIX = "AuthCode ";
    User user;

    /***
     * 레드 그린 사이클 만들자
     */
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
    @DisplayName("로그인을 안한 경우 검증 코드 안보내기")
    void dontSendCode(){
        // given

        // when ,then
        assertThatThrownBy(() -> {
            if (!user.getAuthorities().isEmpty()) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("코드 검증")
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

    @Test
    @DisplayName("검증 코드가 달라서 실패")
    void diffCode() throws MessagingException {
        // given
        testUtils.login(user);

        String authCode = userMailService.sendCode(user.getEmail());
        int testCode = 12345678;

        // when

        // then
        assertThatThrownBy(() -> {
            if (!authCode.equals(testCode)) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("레디스 코드가 만료되어 검증 실패")
    void expireCode(){
        // given
        testUtils.login(user);
        String email = "jh485200@gmail.com";

        // when
        redisService.expireValues(AUTH_CODE_PREFIX + email, 0);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        // then
        assertThatThrownBy(() -> {
            if (!redisService.checkExistsValue(redisAuthCode)) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("코드가 존재하지 않아서 검증 실패")
    void failVerify(){
        // given
        testUtils.login(user);
        String email = "jh485200@gmail.com";

        // when
        redisService.deleteValues(AUTH_CODE_PREFIX + email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        // then
        assertThatThrownBy(() -> {
            if (!redisService.checkExistsValue(redisAuthCode)) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }
}