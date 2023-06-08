package com.example.healthgenie.controller;

import com.example.healthgenie.dto.userLoginDto;
import com.example.healthgenie.dto.userMailAuthDto;
import com.example.healthgenie.dto.userRegisterDto;
import com.example.healthgenie.exception.UserEmailErrorResult;
import com.example.healthgenie.exception.UserEmailException;
import com.example.healthgenie.global.utils.basicUtils;
import com.example.healthgenie.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/User/signup") // http://localhost:1234/User/signup
    public ResponseEntity<String> signUp(@RequestBody userRegisterDto request, @RequestBody userMailAuthDto requestData) {
        return userService.signUp(request, requestData);
    }

    // 이메일 인증
    @PostMapping("/User/authMail") // http://localhost:1234/User/authMail
    public String[] authMail(@RequestBody String email, @RequestBody userMailAuthDto requestData) {
        return userService.authMail(email, requestData);
    }

    // 로그인
    @PostMapping("/User/login") // http://localhost:1234/User/login
    public ResponseEntity<String> login(@RequestBody userLoginDto request) {
        return userService.login(request);
    }

}
