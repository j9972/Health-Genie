package com.example.healthgenie.boundedContext.email.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.email.dto.MailRequestDto;
import com.example.healthgenie.boundedContext.email.service.UserMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/auth/mail")
public class MailController {

    private final UserMailService userMailService;

    @Value("${univCert.key}")
    private String KEY;

    // 이메일 코드전송, 이메일 유효성검사
//    @PostMapping("/send") // http://localhost:1234/auth/mail/send
//    public ResponseEntity<Result> authMail(@RequestBody MailRequestDto dto)  throws IOException {
//
//        log.info("execute controller");
//
//        log.info("dto : {}", dto);
//
//        log.info("key : {} , email : {}, universityName : {}", KEY, dto.getEmail(), dto.getUniversityName());
//
//        Map<String, Object> certify = userMailService.certify(KEY, dto.getEmail(), dto.getUniversityName());
//
//        log.info("certify : {}", certify);
//
//        return ResponseEntity.ok(Result.of("이메일이 성공적으로 보내졌습니다."));
//    }

    @PostMapping("/send") // http://localhost:1234/auth/mail/send
    public ResponseEntity<Result> authMail(@RequestBody String email) {

        userMailService.sendCode(email);

        return ResponseEntity.ok(Result.of("이메일이 성공적으로 보내졌습니다."));
    }

    //이메일 코드검증  -> accessToken 필요
    @GetMapping("/verify") // http://localhost:1234/auth/mail/verify
    public ResponseEntity<Result> validMailCode(@RequestParam("email") String email,
                                        @RequestParam("authCode") String authCode) throws IOException {

        boolean verify = userMailService.verify(email, authCode);

        return ResponseEntity.ok(Result.of(verify ? "검증이 성공했습니다" : "검증이 실패했습니다"));

    }
}
