package com.example.healthgenie.service;

import com.example.healthgenie.dto.userRegisterDto;
import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
// @SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;


    @DisplayName("회원 가입")
    @Test
    void signUp() {
        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
//        ResponseEntity<String> user = userService.signUp(userRegisterDto.builder()
//                .email("test@email.com")
//                .name("testName")
//                .password("testPwd")
//                .uniName("kyung")
//                .role(Role.USER)
//                .build()
//        );

        // When -> action 을 하는 테스트 실행 -> 하나의 메서드만 & 가장 중요하지만 짧다
        User result = userRepository.findByEmail("test@email.com").get();

        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
        assertThat(user).isEqualTo(result);
    }
}