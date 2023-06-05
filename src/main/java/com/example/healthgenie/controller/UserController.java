package com.example.healthgenie.controller;

import com.example.healthgenie.dto.userLoginDto;
import com.example.healthgenie.dto.userRegisterDto;
import com.example.healthgenie.global.constants.constant;
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
    public ResponseEntity<String> signUp(@RequestBody userRegisterDto request) {
        try{
            return userService.signUp(request);
        } catch (Exception ex) {
            log.error("{}", ex.getMessage());
            ex.printStackTrace();
        }
        return basicUtils.getResponseEntity(constant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/User/login") // http://localhost:1234/User/login
    public ResponseEntity<String> login(@RequestBody userLoginDto request) {
        try {
            return userService.login(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return basicUtils.getResponseEntity(constant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 트레이너 회원가입
//    @PostMapping("/User/signup/trainer") // http://localhost:1234/User/signup/trainer
//    public ResponseEntity<String> trainerSignUp(@RequestBody userRegisterDto request) {
//        try{
//            return userService.trainerSignUp(request);
//        } catch (Exception ex) {
//            log.error("{}", ex.getMessage());
//            ex.printStackTrace();
//        }
//        return basicUtils.getResponseEntity(constant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
