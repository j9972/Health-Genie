package com.example.healthgenie.repository;

import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.domain.user.dto.userRegisterDto;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.exception.GlobalExceptionHandler;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Slf4j
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;


    @Test
    public void save(){

        final User usr = userRepository.save(User.builder().email("email@naver.com").role(Role.USER).name("test").uniName("a").password("1234").provider("aka").refreshTokenId("dfka").build());

        assertThat(usr.getId()).isEqualTo(1L);
    }


    @DisplayName("회원 가입")
    @Test
    public void signUp() {
        log.info("test - signUp");

        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
        User user = User.builder()
                .email("email@naver.com")
                .role(Role.USER)
                .name("test")
                .uniName("a")
                .password("1234")
                .provider("aka")
                .refreshTokenId("dfka")
                .build();

        User res = userRepository.save(user);

        //final User res = userRepository.save(User.builder().id(1L).email("email@naver.com").role(Role.USER).name("test").uniName("a").password("1234").provider("aka").refreshTokenId("dfka").build());

        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
        assertThat(res.getId()).isNotNull();
        assertThat(res.getEmail()).isEqualTo("test@naver.com");
        assertThat(res.getPassword()).isEqualTo("1234");

    }

    @Test
    public void 회원존재여부() {
        log.info("test - 회원존재여부");

        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
        User user = User.builder()
                .email("email@naver.com")
                .role(Role.USER)
                .name("test")
                .uniName("a")
                .password("1234")
                .provider("aka")
                .refreshTokenId("dfka")
                .build();

        User res = userRepository.save(user);
        User findUser = userRepository.findByEmailId("email@naver.com");

        //final User res = userRepository.save(User.builder().id(1L).email("email@naver.com").role(Role.USER).name("test").uniName("a").password("1234").provider("aka").refreshTokenId("dfka").build());

        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isNotNull();
        assertThat(findUser).isEqualTo(res);

    }

    @Test
    @DisplayName("로그인")
    public void login() {
    }
}