package com.example.healthgenie.Email.controller;

import com.example.healthgenie.Email.service.MailService;
import com.example.healthgenie.Email.service.UserMailService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity authMail(@RequestBody String email) throws MessagingException {

        userMailService.sendCode(email);

        return new ResponseEntity("이메일이 성공적으로 보내쟜습니다", HttpStatus.OK);
    }

    //이메일 코드검증
    @GetMapping("/verify") // http://localhost:1234/auth/mail/verify
    public ResponseEntity validMailCode(@RequestParam("email") String email,
                                        @RequestParam("authCode") String authCode){
        boolean result = userMailService.verify(email, authCode);
        if (result) {
            return new ResponseEntity("authcode is correct",HttpStatus.OK);
        }
        return new ResponseEntity("authcode is wrong ",HttpStatus.BAD_REQUEST);
    }
}
