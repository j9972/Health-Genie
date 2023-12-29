package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.chat.dto.ChatMessageRequest;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChatMessageServiceTest {

    @Autowired
    MessageService messageService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    TestKrUtils testKrUtils;

    User sender;
    User receiver;
    User other;
    ChatRoom room;

    @BeforeEach
    void before() {
        // 사용자 생성
        sender = testKrUtils.createUser("sender1", Role.USER, "sender1@test.com");
        receiver = testKrUtils.createUser("receiver", Role.TRAINER, "receiver1@test.com");
        other = testKrUtils.createUser("other1", Role.EMPTY, "other1@test.com");

        // 채팅방 생성
        room = testKrUtils.createChatRoom(sender, receiver);
    }

    @Test
    @DisplayName("정상적으로 채팅방을 입장한다.")
    void enter() {
        // given
        testKrUtils.login(sender);

        ChatMessageRequest enterRequest = testKrUtils.createMessageRequest(sender.getId(), room.getId());

        // when

        // then
        assertThatCode(() -> messageService.enter(enterRequest)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 채팅방을 입장하면 예외를 발생시킨다.")
    void enter2() {
        // given
        ChatMessageRequest enterRequest = testKrUtils.createMessageRequest(999L, room.getId());

        // when

        // then
        assertThatThrownBy(() -> messageService.enter(enterRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방을 입장하면 예외를 발생시킨다.")
    void enter3() {
        // given
        ChatMessageRequest enterRequest = testKrUtils.createMessageRequest(sender.getId(), 999L);

        // when

        // then
        assertThatThrownBy(() -> messageService.enter(enterRequest))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("정상적으로 메세지를 보낸다.")
    void sendMessage() {
        // given
        testKrUtils.login(sender);

        ChatMessageRequest sendRequest = testKrUtils.createMessageRequest("테스트 메세지 발송", sender.getId(), room.getId());

        // when

        // then
        assertThatCode(() -> messageService.sendMessage(sendRequest)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 메세지를 보내면 예외를 발생시킨다.")
    void sendMessage2() {
        // given
        ChatMessageRequest sendRequest = testKrUtils.createMessageRequest("테스트 메세지 발송", 999L, room.getId());

        // when

        // then
        assertThatThrownBy(() -> messageService.sendMessage(sendRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방에 메세지를 보내면 예외를 발생시킨다.")
    void sendMessage3() {
        // given
        ChatMessageRequest sendRequest = testKrUtils.createMessageRequest("테스트 메세지 발송", sender.getId(), 999L);

        // when

        // then
        assertThatThrownBy(() -> messageService.sendMessage(sendRequest))
                .isInstanceOf(ChatException.class);
    }
}