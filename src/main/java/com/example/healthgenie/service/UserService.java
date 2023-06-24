package com.example.healthgenie.service;

import com.example.healthgenie.Email.EmailValidator;
import com.example.healthgenie.domain.user.dto.KakaoProfile;
import com.example.healthgenie.domain.user.dto.userLoginDto;
import com.example.healthgenie.domain.user.dto.userLoginResponseDto;
import com.example.healthgenie.domain.user.dto.userRegisterDto;
import com.example.healthgenie.domain.user.entity.RefreshToken;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.*;
import com.example.healthgenie.global.config.JwtUtil;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final EmailValidator emailValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public Long signUp(userRegisterDto request) {
        log.info("Inside  signUp {}", request.getEmail());
        try {

            // 유저 중복 체크
            boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();

            if (userExists) {
                log.info("이메일 중복 : " + request.getEmail());
                throw new UserEmailException(UserEmailErrorResult.DUPLICATED_EMAIL);
            }

//            String hashPWD = passwordEncoder.encode(request.getPassword());

//            User user = User.builder()
//                    .name(request.getName())
//                    .password(hashPWD)
//                    .email(request.getEmail())
//                    .uniName(request.getUniName())
//                    .role(request.getRole())
//                    .build();

            User user = request.toEntity(passwordEncoder);
            return userRepository.save(user).getId();

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UserEmailException(UserEmailErrorResult.UNkNOWN_EXCEPTION);
    }

    public String authMail(String email) {

        // 난수 생성
        String authCode = emailService.createCode();

        //생성한 난수 저장
        emailService.saveCode(authCode);
        log.info("authCode {} ", authCode);

        //난수코드 전송
        emailService.sendSimpleMessage(email, "This is AuthCode", "This confirm Number : "+ authCode);

        return authCode;
    }


    public ResponseEntity<String> login(userLoginDto request) {
        log.info("Inside login check");

        try {
            // email이 없을 경우 Exception이 발생한다. Global Exception에 대한 처리가 필요하다.
            Optional<User> user = userRepository.findByEmail(request.getEmail());

            if(user.isEmpty()){
                throw new CommonException(CommonErrorResult.UNAUTHORIZED);
            }
            log.info("user : {}", user);

            if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
                throw new UserEmailException(UserEmailErrorResult.INVALID_EMAIL);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );


            if(auth.isAuthenticated()) {

                String accessToken = jwtUtil.createAccessToken(user.get().getEmail(), user.get().getRole().toString());
                String refreshToken = jwtUtil.createRefreshToken(user.get().getEmail(), user.get().getRole().toString());

                // RefreshToken을 DB에 저장한다. 성능 때문에 DB가 아니라 Redis에 저장하는 것이 좋다.
                RefreshToken refreshTokenEntity = new RefreshToken();
                refreshTokenEntity.setToken(refreshToken);
                refreshTokenEntity.setId(user.get().getId());
                refreshTokenService.addRefreshToken(refreshTokenEntity);

                userLoginResponseDto loginResponse = userLoginResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user_id(user.get().getId())
                        .email(user.get().getEmail())
                        .build();

                //TODO : builder()를 데이터로 뽑는 방법이다
                return new ResponseEntity(loginResponse, HttpStatus.OK);

            } else {
                log.info("auth 인증 안됨");
                throw new UserEmailException(UserEmailErrorResult.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("{}", e);
        }
        throw new UserEmailException(UserEmailErrorResult.BAD_CREDENTIALS);
    }


    @Transactional
    public Long socialSignup(userRegisterDto userSignupRequestDto) {
        if (userRepository
                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        return userRepository.save(userSignupRequestDto.toEntity()).getId();
    }

    @Transactional
    public ResponseEntity socialLogin(KakaoProfile kakaoProfile){
        Optional<User> user  = userRepository.findByEmailAndProvider(kakaoProfile.getKakao_account().getEmail(), "kakao");
        if(user.isEmpty()){
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }

        String accessToken = jwtUtil.createAccessToken(user.get().getEmail(), user.get().getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(user.get().getEmail(), user.get().getRole().toString());

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setId(user.get().getId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        userLoginResponseDto loginResponse = userLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user_id(user.get().getId())
                .email(user.get().getEmail())
                .build();

        //TODO : builder()를 데이터로 뽑는 방법이다
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }


}
