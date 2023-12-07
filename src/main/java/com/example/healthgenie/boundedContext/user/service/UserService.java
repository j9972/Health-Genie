package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.user.dto.UserRegisterDto;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;

public interface UserService {

    User signUp(UserRegisterDto userSignupRequestDto);

    User findById(Long userId);

    void updateRole(Role role);

    void updateNickname(String nickname);
}