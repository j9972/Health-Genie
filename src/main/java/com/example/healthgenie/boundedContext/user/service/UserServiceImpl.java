package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.user.dto.UserRegisterDto;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User signUp(UserRegisterDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .authProvider(dto.getAuthProvider())
                .uniName(dto.getUniName())
                .role(dto.getRole())
                .level(dto.getLevel())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public void updateRole(Role role) {
        User currentUser = SecurityUtils.getCurrentUser();

        currentUser.updateRole(role);
    }
}