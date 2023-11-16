package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.SignUpRequest;
import com.example.healthgenie.domain.user.dto.UserLoginResponseDto;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.User;

public interface UserService {


    User socialSignUp(UserRegisterDto userSignupRequestDto);
    UserLoginResponseDto socialLogin(String Email);
    UserLoginResponseDto addDummyUser(UserRegisterDto dto);
    Long createUser(SignUpRequest signUpRequest);



//    public List<User> findMembers();
//    public Optional<User> findOne(Long userId);

}
