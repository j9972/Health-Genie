package com.example.healthgenie.boundedContext.email.service;

import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    private final UserService userService;


    @Transactional
    public void updateUniv(String univ_name, Long userId) {
        User user = userService.findById(userId);
        userService.update(user, univ_name, null);
    }

    @Transactional
    public void updateUnivVerify(Long userId) {
        User user = userService.findById(userId);
        userService.update(user, null, true);
    }
}
