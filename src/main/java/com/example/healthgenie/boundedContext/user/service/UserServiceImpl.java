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

import java.util.Random;

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
        String defaultNickname = createOriginalNickname();

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(defaultNickname)
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
        Long currentUserId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(currentUserId).orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        user.updateRole(role);
    }

    @Transactional
    @Override
    public void updateNickname(String nickname) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(currentUserId).orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        user.updateNickname(nickname);
    }

    private String createOriginalNickname() {
        String nickname;

        Random random = new Random();
        while(true) {
            String temp = String.valueOf(random.nextInt(99999999) + 1);

            if (!userRepository.existsByNickname(temp)) {
                nickname = temp;
                break;
            }
        }

        return nickname;
    }
}