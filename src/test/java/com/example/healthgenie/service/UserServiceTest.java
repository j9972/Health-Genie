package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@SpringBootTest
@Service
@Transactional
@RequiredArgsConstructor
@ExtendWith(SpringExtension.class) // @RunWith(SpringRunner.class)의 junit 5 버전
class UserServiceTest {


    private final UserRepository userRepository;


    private final UserService userService;


    @DisplayName("회원 가입")
    @Test
    public void signUp() {
        System.out.println("test");
        log.info("test - signUp");

        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
        User user = User.builder()
                .email("email@naver.com")
                .password("1234")
                .uniName("knu")
                .name("test")
                .role(Role.ADMIN)
                .build();

        log.info("user : {}", user);

        // When -> action 을 하는 테스트 실행 -> 하나의 메서드만 & 가장 중요하지만 짧다
//         Long saveId = userRepository.save(user).getId();
//         log.info("id", saveId);
         // User saveUser = userRepository.save(user);

        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
//        User findMember = userRepository.findById(saveId).get();
        // User findMember = userRepository.findByEmail(saveUser.getEmail()).get();
//        assertThat(user.getName()).isEqualTo(findMember.getName());

    }

    @Test
    @DisplayName("회원 이름으로 조회 테스트")
    @Transactional
    public void findUserByEmail() {

        // given
        User insertUser = userRepository.save(User.builder()
                .password("javascript")
                .email("test@naver.com")
                .name("RexSeo")
                .uniName("knu")
                .role(Role.USER)
                .build());

        // when
        Optional<User> findUser = userRepository.findByEmail("test@naver.com");

        // then
        assertThat(insertUser).isEqualTo(findUser);
    }
}