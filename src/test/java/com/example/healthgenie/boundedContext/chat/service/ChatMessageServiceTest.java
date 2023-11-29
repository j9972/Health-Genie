package com.example.healthgenie.boundedContext.chat.service;

import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.boundedContext.chat.dto.MessageRequest;
import com.example.healthgenie.boundedContext.chat.dto.MessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.repository.ChatRoomRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    User sender;
    User receiver;
    User other;
    ChatRoom room;

    @BeforeEach
    void before() {
        // 사용자 생성
        sender = createUser("sender1", Role.USER, "sender1@test.com");
        receiver = createUser("receiver1", Role.TRAINER, "receiver1@test.com");
        other = createUser("other1", Role.EMPTY, "other1@test.com");

        // 채팅방 생성
        room = createChatRoom(sender, receiver);
    }

    @Test
    @DisplayName("정상적으로 메세지를 보낸다.")
    void rightMessage() {
        // given
        login(sender);

        MessageRequest request = createMessageRequest(room.getId(), "정상적으로 메세지 보내기!", sender.getEmail());

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
        login(sender);

        MessageRequest request = createMessageRequest(999L, "잘못된 채팅방으로 메세지 보내기!", sender.getEmail());

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
        login(other);

        // when
        assertThatThrownBy(() -> chatMessageService.getMessages(room.getId(), 0, 10))
                .isInstanceOf(ChatException.class);

        // then
    }

    private MessageRequest createMessageRequest(Long roomId, String content, String senderEmail) {
        return MessageRequest.builder()
                .content(content)
                .roomId(roomId)
                .senderEmail(senderEmail)
                .build();
    }

    private User createUser(String name, Role role, String email) {
        User user = User.builder()
                .name(name)
                .role(role)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    private ChatRoom createChatRoom(User sender, User receiver) {
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    private void login(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }
}