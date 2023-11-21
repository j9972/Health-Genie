package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.TestSignUpRequest;
import com.example.healthgenie.domain.user.dto.TestSignUpResponse;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;

public interface UserService {

    User signUp(UserRegisterDto userSignupRequestDto);

    TestSignUpResponse createUser(TestSignUpRequest signUpRequest);

    User findById(Long userId);

    void updateRole(Long userId, Role role);

    User findByEmail(String email);
}