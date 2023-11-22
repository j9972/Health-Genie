package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.user.dto.TestSignUpRequest;
import com.example.healthgenie.boundedContext.user.dto.TestSignUpResponse;
import com.example.healthgenie.boundedContext.user.dto.UserRegisterDto;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;

public interface UserService {

    User signUp(UserRegisterDto userSignupRequestDto);

    TestSignUpResponse createUser(TestSignUpRequest signUpRequest);

    User findById(Long userId);

    void updateRole(Long userId, Role role);

    User findByEmail(String email);
}