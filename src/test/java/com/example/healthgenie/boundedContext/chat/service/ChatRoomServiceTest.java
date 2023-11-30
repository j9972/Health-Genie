package com.example.healthgenie.boundedContext.chat.service;


import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.RoomResponse;
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
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;
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
    }

    @Test
    @DisplayName("정상적으로 새로운 채팅방을 생성한다.")
    void rightCreateRoom() {
        // given
        login(sender);

        RoomRequest request = createRoomRequest(receiver.getEmail());

        // when
        Long createRoomId = chatRoomService.joinChatRoom(request);
        ChatRoom findRoom = chatRoomRepository.findById(createRoomId).get();

        // then
        assertThat(createRoomId).isEqualTo(findRoom.getId());
    }

    @Test
    @DisplayName("존재하지 않는 사용자와 채팅방을 생성한다.")
    void notExistReceiver() {
        // given
        login(sender);

        String notExistUserEmail = "none@test.com";
        RoomRequest request = createRoomRequest(notExistUserEmail);

        // when
        assertThatThrownBy(() -> chatRoomService.joinChatRoom(request))
                .isInstanceOf(CommonException.class);

        // then
    }

    @Test
    @DisplayName("자기 자신과 채팅방을 생성한다.")
    void selfChat() {
        // given
        login(sender);

        RoomRequest request = createRoomRequest(sender.getEmail());

        // when
        assertThatThrownBy(() -> chatRoomService.joinChatRoom(request))
                .isInstanceOf(ChatException.class);

        // then
    }

    @Test
    @DisplayName("정상적으로 기존의 채팅방에 입장한다.")
    void rightAlreadyRoom() {
        // given
        login(sender);

        RoomRequest request = createRoomRequest(receiver.getEmail());

        // when
        Long createRoomId = chatRoomService.joinChatRoom(request);
        Long alreadyRoomId = chatRoomService.joinChatRoom(request);

        // then
        assertThat(createRoomId).isEqualTo(alreadyRoomId);
    }

    @Test
    @DisplayName("현재 사용자의 모든 채팅방을 조회한다.")
    void getChatRooms() {
        // given
        login(sender);

        RoomRequest request1 = createRoomRequest(receiver.getEmail());
        RoomRequest request2 = createRoomRequest(other.getEmail());
        chatRoomService.joinChatRoom(request1);
        chatRoomService.joinChatRoom(request2);

        // when
        List<RoomResponse> rooms = chatRoomService.getRoomList();

        // then
        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("정상적으로 채팅방 입장 시 상세 보기")
    void getChatRoomDetail() {
        // given
        login(sender);

        RoomRequest request = createRoomRequest(receiver.getEmail());

        Long roomId = chatRoomService.joinChatRoom(request);

        // when
        RoomResponse response = chatRoomService.getRoomDetail(roomId);

        // then
        assertThat(response.getSenderEmail()).isEqualTo(sender.getEmail());
        assertThat(response.getReceiverEmail()).isEqualTo(receiver.getEmail());
    }

    @Test
    @DisplayName("채팅방에 참여하지 않은 사용자가 채팅방 입장 시 상세 보기")
    void wrongUserChatDetail() {
        // given
        login(sender);

        RoomRequest request = createRoomRequest(receiver.getEmail());

        Long roomId = chatRoomService.joinChatRoom(request);

        // when
        login(other);

        // then
        assertThatThrownBy(() -> chatRoomService.getRoomDetail(roomId))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 상세 보기")
    void notExistChatRoom() {
        // given
        login(sender);

        // when
        assertThatThrownBy(() -> chatRoomService.getRoomDetail(999L))
                .isInstanceOf(ChatException.class);

        // then
    }

    private RoomRequest createRoomRequest(String receiverEmail) {
        return RoomRequest.builder()
                .receiverEmail(receiverEmail)
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

    private void login(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }
}