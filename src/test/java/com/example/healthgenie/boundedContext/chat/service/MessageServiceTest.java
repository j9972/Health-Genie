package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.entity.Message;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MessageServiceTest {

    @Autowired
    MessageService messageService;
    @Autowired
    TestKrUtils testKrUtils;

    User user1;
    User user2;
    User user3;
    Room user1AndUser2Room;

    @BeforeEach
    void before() {
        // 사용자 생성
        user1 = testKrUtils.createUser("sender@test.com", "sender", AuthProvider.KAKAO, Role.USER);
        user2 = testKrUtils.createUser("receiver@test.com", "receiver", AuthProvider.GOOGLE, Role.TRAINER);
        user3 = testKrUtils.createUser("other@test.com", "other", AuthProvider.EMPTY, Role.ADMIN);
        // user1, user2의 기본 채팅방
        user1AndUser2Room = testKrUtils.createRoom(user1, user2.getId());
    }

    @Test
    @DisplayName("메세지 저장")
    void save() {
        // given
        MessageRequest request = MessageRequest.builder().content("새 메세지입니다.").senderId(user1.getId()).build();

        // when
        Message message = messageService.save(user1AndUser2Room.getId(), request);

        // then
        assertThat(message.getSender().getId()).isEqualTo(user1.getId());
        assertThat(message.getContent()).isEqualTo("새 메세지입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 메세지를 보냄")
    void save_notExistsSender_exception() {
        // given
        MessageRequest request = MessageRequest.builder().content("새 메세지입니다.").senderId(999L).build();

        // when

        // then
        assertThatThrownBy(() -> messageService.save(user1AndUser2Room.getId(), request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방에 메세지를 보냄")
    void save_notExistsRoom_exception() {
        // given
        MessageRequest request = MessageRequest.builder().content("새 메세지입니다.").senderId(user1.getId()).build();

        // when

        // then
        assertThatThrownBy(() -> messageService.save(999L, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("해당 채팅방의 전체 메세지 조회")
    void findAll() {
        // given
        for(int i=0; i<10; i++) {
            testKrUtils.createMessage(user1AndUser2Room.getId(), user1.getId(), "새 메세지 " + i);
        }

        // when
        List<Message> messages = messageService.findAll(user1AndUser2Room.getId(), user1);

        // then
        assertThat(messages.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("해당 채팅방에 참여하지 않는 사용자가 메세지를 조회함")
    void findAll_notParticipateUser_exception() {
        // given
        for(int i=0; i<10; i++) {
            testKrUtils.createMessage(user1AndUser2Room.getId(), user1.getId(), "새 메세지 " + i);
        }

        // when

        // then
        assertThatThrownBy(() -> messageService.findAll(user1AndUser2Room.getId(), user3))
                .isInstanceOf(CustomException.class);
    }
}