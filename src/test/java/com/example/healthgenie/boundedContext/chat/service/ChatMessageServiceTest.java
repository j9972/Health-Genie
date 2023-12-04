package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.util.TestUtils;
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
class ChatMessageServiceTest {

    @Autowired
    ChatMessageService chatMessageService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    TestUtils testUtils;

    User sender;
    User receiver;
    User other;
    ChatRoom room;

    @BeforeEach
    void before() {
        // 사용자 생성
        sender = testUtils.createUser("sender1", Role.USER, "sender1@test.com");
        receiver = testUtils.createUser("receiver1", Role.TRAINER, "receiver1@test.com");
        other = testUtils.createUser("other1", Role.EMPTY, "other1@test.com");

        // 채팅방 생성
        room = testUtils.createChatRoom(sender, receiver);
    }

    @Test
    @DisplayName("정상적으로 메세지를 보낸다.")
    void rightMessage() {
        // given
        testUtils.login(sender);

        MessageRequest request = testUtils.createMessageRequest(room.getId(), "정상적으로 메세지 보내기!", sender.getNickname());

        // when
        chatMessageService.sendMessage(request);

        // then
        List<MessageResponse> messages = chatMessageService.getMessages(room.getId(), 0, 10);

        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("잘못된 채팅방으로 메세지를 보낸다.")
    void wrongMessage() {
        // given
        testUtils.login(sender);

        MessageRequest request = testUtils.createMessageRequest(999L, "잘못된 채팅방으로 메세지 보내기!", sender.getNickname());

        // when
        assertThatThrownBy(() -> chatMessageService.sendMessage(request))
                .isInstanceOf(ChatException.class);

        // then
        List<MessageResponse> messages = chatMessageService.getMessages(room.getId(), 0, 10);

        assertThat(messages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방에 없는 사용자가 메세지를 확인한다.")
    void notRelatedUser() {
        // given
        testUtils.login(other);

        // when
        assertThatThrownBy(() -> chatMessageService.getMessages(room.getId(), 0, 10))
                .isInstanceOf(ChatException.class);

        // then
    }
}