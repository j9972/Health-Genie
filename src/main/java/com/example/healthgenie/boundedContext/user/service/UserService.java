package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.example.healthgenie.base.exception.UserErrorResult.*;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signUp(String email, String name, AuthProvider authProvider) {
        String defaultNickname = createOriginalNickname();

        User user = User.builder()
                .email(email)
                .name(name)
                .nickname(defaultNickname)
                .authProvider(authProvider)
                .uniName("")
                .role(Role.EMPTY)
                .level(Level.EMPTY)
                .build();

        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        user.updateRole(role);

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(CommonErrorResult.USER_NOT_FOUND));

        if(userRepository.existsByNickname(nickname)) {
            throw new UserException(DUPLICATED_NICKNAME);
        }

        user.updateNickname(nickname);

        return UserResponse.of(user);
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