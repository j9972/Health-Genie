package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.SignUpRequest;
import com.example.healthgenie.domain.user.dto.UserLoginResponseDto;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.RefreshToken;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.global.config.JwtUtil;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.healthgenie.domain.user.entity.AuthProvider.KAKAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

/*
    @Transactional
    @Override
    public Long signUp(userRegisterDto request) {
        log.info("Inside  signUp {}", request.getEmail());
        try {

            // 유저 중복 체크
            boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();

            if (userExists) {
                log.info("이메일 중복 : " + request.getEmail());
                throw new UserEmailException(UserEmailErrorResult.DUPLICATED_EMAIL);
            }

            request.setPassword(encodedPwd((request.getPassword())));
            User user = request.toEntity();
            return userRepository.save(user).getId();

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UserEmailException(UserEmailErrorResult.UNkNOWN_EXCEPTION);
    }
    @Override
    public String authMail(String email) {
        log.info(email);

        // 난수 생성
        String authCode = emailService.createCode();

        //생성한 난수 저장
        emailService.saveCode(authCode);
        log.info("authCode {} ", authCode);

        //난수코드 전송
        emailService.sendSimpleMessage(email, "This is AuthCode", "This confirm Number : "+ authCode);

        return authCode;
    }

 */
    @Transactional
    @Override
    public User socialSignUp(UserRegisterDto dto) {
//        log.info(userSignupRequestDto.toString());
//        UserLoginResponseDto result = null;
//        if (userRepository
//                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
//                .isPresent()
//        ) {
//            //기존회원이 존재한다면 소셜로그인으로 넘어간다.
//            result = socialLogin(userSignupRequestDto.getEmail());
//        }
//        else{
//            Long id = userRepository.save(userSignupRequestDto.tosocialEntity()).getId();
//            if(id !=null){
//                result = RefreshAccessIssue(userSignupRequestDto.getEmail(),userSignupRequestDto.getRole().toString(),id);
//            }
//            else{
//                log.info("error_신규 회원가입실패");
//            }
//        }
//        return result;
        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .authProvider(dto.getAuthProvider())
                .uniName(dto.getUniName())
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }

    public UserLoginResponseDto RefreshAccessIssue(String email, String role, Long Id){
        String accessToken = jwtUtil.createAccessToken(email,role);
        String refreshToken = jwtUtil.createRefreshToken(email,role);
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setId(Id);
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        UserLoginResponseDto loginResponse = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user_id(Id)
                .email(email)
                .build();

        return loginResponse;
    }

    @Transactional
    @Override
    public UserLoginResponseDto socialLogin(String email){
        Optional<User> user  = userRepository.findByEmailAndAuthProvider(email, KAKAO);
        if(user.isEmpty()){
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }
        UserLoginResponseDto loginResponse = RefreshAccessIssue(user.get().getEmail(), user.get().getRole().toString(),user.get().getId());
        if(loginResponse ==null){
            log.info("error_기존 로그인_토큰재발급 불가");
        }
        //TODO : builder()를 데이터로 뽑는 방법이다
        return loginResponse;
    }


    public UserLoginResponseDto addDummyUser(UserRegisterDto userSignupRequestDto){
//        UserLoginResponseDto result = null;
//
//        if (userRepository
//                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
//                .isPresent()
//        ) {
//            //기존회원이 존재한다면 소셜로그인으로 넘어간다.
//            result = socialLogin(userSignupRequestDto.getEmail());
//        }
//        else{
//            Long id = userRepository.save(userSignupRequestDto.tosocialEntity()).getId();
//            if(id !=null){
//                result = RefreshAccessIssue(userSignupRequestDto.getEmail(),userSignupRequestDto.getRole().toString(),id);
//            }
//            else{
//                log.info("error_신규 회원가입실패");
//            }
//        }
//        return result;
        return null;
    }

    @Transactional
    public Long createUser(SignUpRequest signUpRequest){
        if(userRepository.existsByEmailAndAuthProvider(signUpRequest.getEmail(), signUpRequest.getAuthProvider())){
            throw new CommonException(CommonErrorResult.BAD_REQUEST);
        }

        return userRepository.save(
                User.builder()
                        .name(signUpRequest.getNickname())
                        .email(signUpRequest.getEmail())
                        .role(Role.USER)
                        .authProvider(signUpRequest.getAuthProvider())
                        .build()
        ).getId();
    }
}
