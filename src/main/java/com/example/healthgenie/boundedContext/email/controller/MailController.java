package com.example.healthgenie.boundedContext.email.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.email.service.UserMailService;
import com.example.healthgenie.boundedContext.email.service.MailService;
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

    // 이메일 코드전송,이메일유효성검사
    @PostMapping("/send") // http://localhost:1234/auth/mail/send
    public ResponseEntity<Result> authMail(@RequestBody String email) throws MessagingException {

        userMailService.sendCode(email);

        return ResponseEntity.ok(Result.of("이메일이 성공적으로 보내졌습니다."));
    }

    //이메일 코드검증
    @GetMapping("/verify") // http://localhost:1234/auth/mail/verify
    public ResponseEntity<Result> validMailCode(@RequestParam("email") String email,
                                        @RequestParam("authCode") String authCode){
        boolean result = userMailService.verify(email, authCode);

        // 검증 실패시 redirect 시켜주세요.
        return ResponseEntity.ok(Result.of(result ? "검증이 실패했습니다" : "검증이 성공했습니다"));

    }
}
