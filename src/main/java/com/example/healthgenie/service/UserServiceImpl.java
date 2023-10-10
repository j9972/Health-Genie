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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    //private final EmailValidator emailValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


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
        //유효성검사
//        boolean isValidEmail = emailValidator.test(email);
//        if (!isValidEmail) {
//            log.info("email is not valid");
//            throw new UserEmailException(UserEmailErrorResult.INVALID_EMAIL);
//        }
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

    @Override
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
    @Override
    public Long socialSignup(userRegisterDto userSignupRequestDto) {
        if (userRepository
                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new CommonException(CommonErrorResult.ITEM_EMPTY);//아이디가 이미 존재합니다.
        return userRepository.save(userSignupRequestDto.tosocialEntity()).getId();
    }

    @Transactional
    @Override
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

    @Override
    @Transactional
    public String getPassword(String name,String email){
        findNameAndEmail(name,email);//이름이메일 확인작업
        String pwd = makePwd();//임시 비번생성
        emailService.pwdSend(email,pwd);
        savePwd(email,pwd);
        return email;
    }

    public String makePwd(){
        String authCode = emailService.createCode();
        return authCode;
    }

    public void savePwd(String email,String pwd){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            String encodedPwd = encodedPwd(pwd);
            user.get().setPassword(encodedPwd);
        }
        else{
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }
    }

    public void findNameAndEmail(String name, String email){
        if(userRepository.findByEmailAndName(email,name).isPresent()) {
            return;
        }
        else{
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);//이름또는 이메일이 정확하지않다
        }
    }

    @Override
    public String ModifiedPassword(String email,String pwd){
        savePwd(email,pwd);
        return email;
    }

    public String encodedPwd(String pwd){
        return passwordEncoder.encode(pwd);
    }

    public List<User> findMembers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }



}
