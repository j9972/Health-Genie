package com.example.healthgenie.global.config;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomerUsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private Optional<User> userDetail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("Inside loadUserByUsername {}", email);

        userDetail = userRepository.findByEmail(email); // email 대신 username 을 쓴것
        if(userDetail.isPresent()) {
            log.info("userDetail {} ", userDetail.get().getEmail());
            return userDetail.get();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    /*
    public String loadUserByRole(String email) throws  UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", email);

        userDetail = userRepository.findByEmail(email); // email 대신 username 을 쓴것
        if(userDetail.isPresent()) {
            log.info("userDetail {} ", userDetail.get().getEmail());
            return userDetail.get().getRole().getCode();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

     */

    public Optional<User> getUserDetail() {
        return userDetail;
    }
}

