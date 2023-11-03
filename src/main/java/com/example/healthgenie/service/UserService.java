package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.userLoginResponseDto;
import com.example.healthgenie.domain.user.dto.userRegisterDto;

public interface UserService {


    userLoginResponseDto socialSignup(userRegisterDto userSignupRequestDto);
    userLoginResponseDto socialLogin(String Email);
    userLoginResponseDto addDummyUser(userRegisterDto dto);



//    public List<User> findMembers();
//    public Optional<User> findOne(Long userId);

}
