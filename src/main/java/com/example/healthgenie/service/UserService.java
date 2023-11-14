package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.UserLoginResponseDto;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;

public interface UserService {


    UserLoginResponseDto socialSignup(UserRegisterDto userSignupRequestDto);
    UserLoginResponseDto socialLogin(String Email);
    UserLoginResponseDto addDummyUser(UserRegisterDto dto);



//    public List<User> findMembers();
//    public Optional<User> findOne(Long userId);

}
