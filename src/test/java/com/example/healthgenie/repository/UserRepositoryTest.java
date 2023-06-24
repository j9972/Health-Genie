package com.example.healthgenie.repository;

import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void save(){

        final User usr = userRepository.save(User.builder().email("emaile").role(Role.USER).name("nam").uniName("n").password("ap").provider("aka").refreshTokenId("dfka").build());

        assertThat(usr.getId()).isEqualTo(1L);
    }
//    @Test

}