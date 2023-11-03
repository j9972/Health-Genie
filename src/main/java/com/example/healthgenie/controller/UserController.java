package com.example.healthgenie.controller;


import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.service.KakaoService;
import com.example.healthgenie.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth")

public class UserController {

    private final UserService userService;
    //private final EmailService emailService;
    private final KakaoService kakaoService;
/*
    // 회원가입
    /*
    @PostMapping("/signup") // http://localhost:1234/api/v1/auth/signup
    public ResponseEntity signUp(@RequestBody userRegisterDto request) {
        Long resultId = userService.signUp(request);
        return new ResponseEntity(resultId,HttpStatus.OK);
    }
     */
    // 이메일 코드전송,이메일유효성검사
    /*
    @PostMapping("/mail/send") // http://localhost:1234/api/v1/auth/mail/send
    public String authMail(@RequestBody emailRequestDto request) {
        return userService.authMail(request.getEmail());
    }
*/
    //이메일 코드검증
    /*
    @PostMapping("/mail/verify") // http://localhost:1234/api/v1/auth/mail/verify
    public ResponseEntity validMailCode(@RequestBody emailRequestDto request){
        String result = emailService.valiedCode(request.getCode());
        return new ResponseEntity(result,HttpStatus.OK);
    }
*/

    //소셜 회원가입 카카오 //여기서 변경해야할 것 회원가입시 기존 유저가 있을 시 로그인으로, 없을시 회원가입으로 그리고 둘다 리프레쉬,엑세스토큰을 리턴
    /*
    @PostMapping("/kakao/signup") // http://localhost:1234/api/v1/auth/signup/kakao
    public ResponseEntity signupBySocial(@RequestBody SocialSignupRequestDto socialSignupRequestDto) {

        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());

        if (kakaoProfile == null) throw new CommonException(CommonErrorResult.ITEM_EMPTY);

        if (kakaoProfile.getKakao_account().getEmail() == null) {
            kakaoService.kakaoUnlink(socialSignupRequestDto.getAccessToken());
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }

        userLoginResponseDto result = userService.socialSignup(userRegisterDto.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .role(Role.USER)
                .name(kakaoProfile.getProperties().getNickname())
                .uniName(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());
        return new ResponseEntity(result,HttpStatus.OK);
    }
    @PostMapping("/add/dummy/user")
    public void addDummyUser(@RequestBody DummyUserDto dto){

        userService.addDummyUser(userRegisterDto.builder()
                .email(dto.getEmail())
                .role(Role.USER)
                .name(dto.getName())
                .uniName("test")
                .provider("kakao")
                .build());
    }


    //비밀번호 찾기

    //이메일, 이름 넣어서 존재하면
    //임시비번 만듬
    //임시비번 이메일 전송
    //임시비번 db에 저장
    /*
    @PostMapping("/pwd")
    public ResponseEntity getPassword(@RequestBody pwdFindRequestDto dto){
        log.info(dto.getEmail());
        String result = userService.getPassword(dto.getName(),dto.getEmail());
        return new ResponseEntity(result,HttpStatus.OK);
    }

     */

    //비밀번호 변경
/*
    @PostMapping("/pwd/1")
    public ResponseEntity ModifiedPassword(@RequestBody pwdModifiedRequestDto dto){
        String result = userService.ModifiedPassword(dto.getEmail(),dto.getPwd());
        return new ResponseEntity(result,HttpStatus.OK);
    }

 */
}
