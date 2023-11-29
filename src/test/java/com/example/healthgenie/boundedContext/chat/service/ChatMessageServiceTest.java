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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    UserDetailsService userDetailsService;

    User sender;
    User receiver;
    User other;
    ChatRoom chatRoom;

    @BeforeEach
    void before() {
        sender = User.builder()
                .name("sender1")
                .role(Role.EMPTY)
                .email("sender1@test.com")
                .build();

        receiver = User.builder()
                .name("receiver1")
                .role(Role.EMPTY)
                .email("receiver1@test.com")
                .build();

        other = User.builder()
                .name("other1")
                .role(Role.EMPTY)
                .email("other1@test.com")
                .build();

        chatRoom = ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        User savedSender = userRepository.save(sender);
        User savedReceiver = userRepository.save(receiver);
        User savedOther = userRepository.save(other);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        System.out.println("=========================");
        System.out.println("savedSender.id = " + savedSender.getId());
        System.out.println("savedReceiver.id = " + savedReceiver.getId());
        System.out.println("savedOther.id = " + savedOther.getId());
        System.out.println("savedChatRoom.id = " + savedChatRoom.getId());
        System.out.println("=========================");

        UserDetails senderDetails = userDetailsService.loadUserByUsername(sender.getEmail());
        UserDetails receiverDetails = userDetailsService.loadUserByUsername(receiver.getEmail());
        UserDetails otherDetails = userDetailsService.loadUserByUsername(other.getEmail());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(senderDetails, "", senderDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(receiverDetails, "", receiverDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(otherDetails, "", otherDetails.getAuthorities()));
    }

    @Test
    @DisplayName("정상적으로 메세지를 보낸다.")
    void sendRightMessage() {
        // given
        MessageRequest request = MessageRequest.builder()
                .content("테스트 쪽지 보내기!")
                .roomId(chatRoom.getId())
                .senderEmail("sender1@test.com")
                .build();

        // when
        chatMessageService.sendMessage(request);

        // then
        List<MessageResponse> messages = chatMessageService.getMessages(chatRoom.getId(), 0, 10);

        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("잘못된 채팅방으로 메세지를 보낸다.")
    void sendWrongMessage() {
        // given
        MessageRequest request = MessageRequest.builder()
                .content("테스트 쪽지 보내기!")
                .roomId(999L)
                .senderEmail("sender1@test.com")
                .build();

        // when
        assertThatThrownBy(() -> chatMessageService.sendMessage(request))
                .isInstanceOf(ChatException.class);

        // then
        List<MessageResponse> messages = chatMessageService.getMessages(chatRoom.getId(), 0, 10);

        assertThat(messages.size()).isEqualTo(0);
    }

    private User createUser(String name, Role role, String email) {
        User user = User.builder()
                .name(name)
                .role(role)
                .email(email)
                .build();

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));

        return user;
    }
}