package com.example.healthgenie.boundedContext.email.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.email.service.RedisService;
import com.example.healthgenie.boundedContext.email.service.UserMailService;
import com.example.healthgenie.boundedContext.email.service.MailService;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth/mail")
public class MailController {

    private final UserMailService userMailService;
    private final MailService mailService;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final RedisService redisService;
    private final UserService userService;

    // 이메일 코드전송,이메일유효성검사
    @PostMapping("/send") // http://localhost:1234/auth/mail/send
    public ResponseEntity<Result> authMail(@RequestBody String email) {

        userMailService.sendCode(email);

        return ResponseEntity.ok(Result.of("이메일이 성공적으로 보내졌습니다."));
    }

    //이메일 코드검증
    @GetMapping("/verify") // http://localhost:1234/auth/mail/verify
    public ResponseEntity<Result> validMailCode(@RequestParam("email") String email,
                                        @RequestParam("authCode") String authCode){

        User currentUser = SecurityUtils.getCurrentUser();
        UserResponse response = null;

        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean result = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if (result) {
            response = userService.successEmailVerify(currentUser.getId());
        }

        return ResponseEntity.ok(Result.of(response));

    }
}
