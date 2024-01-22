package com.example.healthgenie.boundedContext.email.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import com.univcert.api.UnivCert;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserMailServiceTest {

    @Autowired
    TestKrUtils testUtils;

    @Autowired
    TestSyUtils testUtil;

    @Autowired
    UserMailService userMailService;

    @Autowired
    UniDomainService uniDomainService;

    @Autowired
    RedisService redisService;

    @Value("${univCert.key}")
    private String KEY;


    String AUTH_CODE_PREFIX = "AuthCode ";
    User user;
    User user2;
    User user3;

    /***
     * 레드 그린 사이클 만들자
     */
    @BeforeEach
    void before() {
        user = testUtils.createUser("test1", Role.EMPTY, "jsy9972@knu.ac.kr.com");
        user2 = testUtil.createUser("test1","test1", "경북대학교" , "jsy9972@knu.ac.kr");
        user3 = testUtil.createUser("test1", "test1","가나다대학교" , "jsy9972@knu.ac.kr");
    }


    @Test
    @DisplayName("이메일에 검증 코드 보내기")
    void sendCode() throws IOException {
        // given
        testUtils.login(user);
        String uniDomain = uniDomainService.findUniDomain(user2.getUniName());
        Map<String, Object> result = null;

        // when
        if (uniDomainService.checkDomain(user2.getEmail(), uniDomain)) {
            result = UnivCert.certify(KEY,user2.getEmail(), user2.getUniName(), true);
        }

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("학교 도메인이 틀린경우 코드 보내기 실패")
    void wrongDomainForSendCode() {
        // given
        testUtils.login(user);
        String uniDomain = uniDomainService.findUniDomain(user2.getUniName());

        // when

        // then
        assertThatThrownBy(() -> {
            if (!uniDomain.isEmpty()) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
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
    @DisplayName("api 코드 검증")
    void univVerify() throws IOException {
        // given
        testUtils.login(user);

        // when
        userMailService.updateUnivVerify(user.getId());

        // then
        assertThat(user.isEmailVerify()).isTrue();
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