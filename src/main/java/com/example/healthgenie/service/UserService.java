package com.example.healthgenie.service;

import com.example.healthgenie.Email.EmailValidator;
import com.example.healthgenie.dto.userLoginDto;
import com.example.healthgenie.dto.userLoginResponseDto;
import com.example.healthgenie.dto.userRegisterDto;
import com.example.healthgenie.entity.RefreshToken;
import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.global.config.CustomerUsersDetailsService;
import com.example.healthgenie.global.config.JwtUtil;
import com.example.healthgenie.global.constants.constant;
import com.example.healthgenie.global.utils.basicUtils;
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
    private final CustomerUsersDetailsService customerUsersDetailsService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public ResponseEntity<String> signUp(userRegisterDto request) {
        log.info("Inside  signUp {}", request.getEmail());
        try {
            // 이메일 유효성 검사
            boolean isValidEmail = emailValidator.test(request.getEmail());

            if (!isValidEmail) {
                log.info("email not valid");
                return basicUtils.getResponseEntity(constant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            // 유저 중복 체크
            boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();

            if (userExists) {
                log.info("이메일 중복 : " + request.getEmail());
                return basicUtils.getResponseEntity(constant.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
            }

            String hashPWD = passwordEncoder.encode(request.getPassword());

            // 난수 만들어서 이메일 인증을 위함
            String authCode = emailService.createCode();

            log.info("authCode {} ", authCode);

            emailService.sendSimpleMessage(request.getEmail(), "This is AuthCode", "This confirm Number : "+authCode);

            User user = User.builder()
                    .name(request.getName())
                    .password(hashPWD)
                    .email(request.getEmail())
                    .uniName(request.getUniName())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);

            return basicUtils.getResponseEntity("{\"authCode\":\"" + authCode +"\"}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return basicUtils.getResponseEntity(constant.OK_GOOD, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public Optional<User> findOne(String email){
        return userRepository.findByEmail(email);
    }

    public ResponseEntity<String> login(userLoginDto request) {
        log.info("Inside login check");

        try {
            // email이 없을 경우 Exception이 발생한다. Global Exception에 대한 처리가 필요하다.
            Optional<User> user = userRepository.findByEmail(request.getEmail());

            log.info("user : {}", user);

            if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
                return basicUtils.getResponseEntity("email is not valid", HttpStatus.UNAUTHORIZED);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );


            if(auth.isAuthenticated()) {
                log.info("auth 인증 됨");
                log.info("customerUsersDetailsService.getUserDetail() : {} ", customerUsersDetailsService.getUserDetail().get());


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
                return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval ."+"\"}",
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error("{}", e);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);
    }

}
