package com.example.healthgenie.service;

import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.service.UserServiceImpl;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
//@SpringBootTest
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private UserServiceImpl userService;

    private final Long userId = 1L;

    /*
        test code 작성시, setUp 메소드 필수
     */

    /*

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder, authenticationManager,
                emailService, jwtUtil, refreshTokenService);
    }


    @DisplayName("회원 가입")
    @Test
    public void signUp() {
        log.info("test - signUp");

        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
        userRegisterDto saveDto = userRegisterDto.builder().email("test@naver.com").build();
        User saveRes = buildUser(saveDto);
        doReturn(saveRes).when(userRepository).save(any(User.class));

        // When -> action 을 하는 테스트 실행 -> 하나의 메서드만 & 가장 중요하지만 짧다
        Long res = userService.signUp(saveDto);


        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
        assertThat(res).isNotNull();

    }

    @DisplayName("회원 가입 실패")
    @Test
    public void signUpFail() {
        log.info("test - signUp");

        // Given -> 테스트에서 사용되는 변수, 입력 값들을 정의 or Mock 객체 정의
        userRegisterDto saveDto = userRegisterDto.builder().email("test@naver.com").build();
        User saveRes = buildUser(saveDto);
        doReturn(saveRes).when(userRepository).save(any(User.class));

        // When -> action 을 하는 테스트 실행 -> 하나의 메서드만 & 가장 중요하지만 짧다
        Long res = userService.signUp(saveDto);


        // Then -> 실행 결과 검증 , 예상 값과 실제 값 비교, assertThat 사용하기
        assertThat(res).isNotNull();

    }

    private User buildUser(userRegisterDto dto) {
        return User.builder()
                .id(userId)
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .uniName(dto.getUniName())
                .role(dto.getRole())
                .build();
    }

    @Test
    void 중복_회원_예외() {
        //given
        userRegisterDto member1 = userRegisterDto.builder()
                .email("test@naver.com")
                .password("1234")
                .uniName("knu")
                .name("test")
                .role(Role.ADMIN)
                .provider("testCase")
                .build();

        userRegisterDto member2 = userRegisterDto.builder()
                .email("test@naver.com")
                .password("1234")
                .uniName("knu")
                .name("test")
                .role(Role.ADMIN)
                .provider("testCase")
                .build();

        //when
        userService.signUp(member1);

        // 예외처리 테스트 코드
        // IllegalStateException 예외가 실행 되어야 한다
        IllegalStateException returnStatusMessage = assertThrows(IllegalStateException.class, () -> userService.signUp(member2));



        // 다른 예외처리를 했을 때
        // 오류뜸
        //then
        Assertions.assertThat(returnStatusMessage.getMessage()).isEqualTo("이미 존재하는 회원입니다");

    }

     */



    @Test
    @DisplayName("회원 이름으로 조회 테스트")
    public void findUserByEmail() {
        log.info("test1");
        // given

        // when=

        // then
    }
}