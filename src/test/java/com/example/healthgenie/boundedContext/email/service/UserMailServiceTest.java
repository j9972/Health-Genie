package com.example.healthgenie.boundedContext.email.service;

import static com.example.healthgenie.base.exception.ErrorCode.UNKNOWN_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.service.UserService;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import com.univcert.api.UnivCert;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@SpringBootTest
@Transactional
@Slf4j
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
    UserService userService;

    @Value("${univCert.key}")
    private String KEY;


    User user;

    /***
     * 레드 그린 사이클 만들자
     */
    @BeforeEach
    void before() {
        user = testUtils.createUser("jsy9972@knu.ac.kr", "test1", AuthProvider.EMPTY, Role.EMPTY);

        userService.update(user, "경북대학교", null);
    }

    @Test
    @DisplayName("학교 도메인 확인")
    void check_univ_domain() {
        // given
        testUtils.login(user);
        String uniDomain = uniDomainService.findUniDomain(user.getUniName());

        // when
        boolean result = uniDomainService.checkDomain(user.getEmail(), uniDomain);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일에 검증 코드 보내기")
    void send_code() throws IOException {
        // given
        testUtils.login(user);
        Map<String, Object> result = null;

        // when
        result = UnivCert.certify(KEY, user.getEmail(), user.getUniName(), true);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("학교 도메인이 틀린경우 코드 보내기 실패")
    void wrong_domain_for_send_code() {
        // given
        testUtils.login(user);
        String uniDomain = uniDomainService.findUniDomain("가나다대학교");

        // when

        // then
        assertThatThrownBy(() -> {
            if (!StringUtils.hasText(uniDomain)) {
                throw new CustomException(UNKNOWN_EXCEPTION);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("로그인을 안한 경우 검증 코드 안보내기")
    void dont_send_code() {
        // given

        // when ,then
        assertThatThrownBy(() -> {
            if (!user.getAuthorities().isEmpty()) {
                throw new CustomException(UNKNOWN_EXCEPTION);
            }
        }).isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("api 코드 검증")
    void univ_verify() {
        // given
        testUtils.login(user);

        // when
        userMailService.updateUnivVerify(user.getId());

        // then
        assertThat(user.isEmailVerify()).isTrue();
    }

}