package com.example.healthgenie.boundedContext.chat.service;


import com.example.healthgenie.base.exception.ChatException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomRequest;
import com.example.healthgenie.boundedContext.chat.dto.ChatRoomResponse;
import com.example.healthgenie.boundedContext.chat.dto.GetMessageResponse;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoom;
import com.example.healthgenie.boundedContext.chat.entity.ChatRoomUser;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChatRoomServiceTest {

    @Autowired
    RoomService roomService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    TestKrUtils testKrUtils;

    User user1;
    User user2;
    User user3;
    Long user1AndUser3ChatRoomId;

    @BeforeEach
    void before() {
        // 사용자 생성
        user1 = testKrUtils.createUser("sender1", Role.USER, "sender1@test.com");
        user2 = testKrUtils.createUser("receiver", Role.TRAINER, "receiver1@test.com");
        user3 = testKrUtils.createUser("other1", Role.EMPTY, "other1@test.com");
        // user1, user3의 기본 채팅방
        user1AndUser3ChatRoomId = roomService.createChatRoom(testKrUtils.createRoomRequest(user1.getId(), user3.getId()));
    }

    @Test
    @DisplayName("정상적으로 새로운 채팅방을 생성한다.")
    void createChatRoom() {
        // given
        testKrUtils.login(user1);

        ChatRoomRequest request = testKrUtils.createRoomRequest(user1.getId(), user2.getId());

        // when
        Long chatRoomId = roomService.createChatRoom(request);

        ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        List<ChatRoomUser> chatRoomUsers = findChatRoom.getChatRoomUsers();

        // then
        assertThat(chatRoomUsers).extracting(ChatRoomUser::getUser).contains(user1, user2);
    }

    @Test
    @DisplayName("한번이라도 생성한 적 있는 채팅방이라면 그 채팅방을 반환한다.")
    void createChatRoom2() {
        // given
        testKrUtils.login(user1);

        ChatRoomRequest request = testKrUtils.createRoomRequest(user1.getId(), user3.getId());

        // when
        Long roomId = roomService.createChatRoom(request);

        ChatRoom findChatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        List<ChatRoomUser> chatRoomUsers = findChatRoom.getChatRoomUsers();

        // then
        assertThat(chatRoomUsers).extracting(ChatRoomUser::getUser).contains(user1, user3);
    }

    @Test
    @DisplayName("존재하지 않는 사용자와 채팅방을 생성하면 예외를 발생시킨다.")
    void createChatRoom3() {
        // given
        testKrUtils.login(user1);

        ChatRoomRequest request = testKrUtils.createRoomRequest(user1.getId(), 999L);

        // when

        // then
        assertThatThrownBy(() -> roomService.createChatRoom(request))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("정상적으로 채팅방 목록을 조회한다.")
    void getChatRooms() {
        // given
        testKrUtils.login(user1);

        ChatRoomRequest createRequest1 = testKrUtils.createRoomRequest(user1.getId(), user2.getId());
        ChatRoomRequest createRequest2 = testKrUtils.createRoomRequest(user1.getId(), user3.getId());

        roomService.createChatRoom(createRequest1);
        roomService.createChatRoom(createRequest2);

        // when
        List<ChatRoomResponse> responses = roomService.findAll(user1, PageRequest.of(0, 10));

        // then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("정상적으로 채팅방 내역을 조회한다.")
    void getMessages() {
        // given
        testKrUtils.login(user1);

        ChatRoomRequest createRequest = testKrUtils.createRoomRequest(user1.getId(), user2.getId());
        Long roomId = roomService.createChatRoom(createRequest);

        // when
        List<GetMessageResponse> responses = roomService.getMessages(roomId, user1);

        // then
        assertThat(responses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않거나 비활성화 상태인 채팅방 내역을 조회하면 예외를 발생시킨다.")
    void getMessages2() {
        // given
        testKrUtils.login(user1);

        // when

        // then
        assertThatThrownBy(() -> roomService.getMessages(999L, user1))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("채팅방에 참여하지 않은 사용자가 채팅방 내역을 조회하면 예외를 발생시킨다.")
    void getMessages3() {
        // given
        testKrUtils.login(user2);

        // when

        // then
        assertThatThrownBy(() -> roomService.getMessages(user1AndUser3ChatRoomId, user2))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("정상적으로 채팅방을 삭제한다.")
    void deleteChatRoom() {
        // given
        testKrUtils.login(user1);

        // when
        roomService.deleteChatRoom(user1AndUser3ChatRoomId, user1);

        List<ChatRoomResponse> deleted = roomService.findAll(user1, PageRequest.of(0, 10));
        List<ChatRoomResponse> notDeleted = roomService.findAll(user3, PageRequest.of(0, 10));

        // then
        assertThat(deleted.size()).isEqualTo(0);
        assertThat(notDeleted.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("정상적으로 양쪽 모두 채팅방을 삭제하면 채팅방은 비활성화된다.")
    void deleteChatRoom2() {
        // given
        testKrUtils.login(user1);

        // when
        roomService.deleteChatRoom(user1AndUser3ChatRoomId, user1);
        roomService.deleteChatRoom(user1AndUser3ChatRoomId, user3);

        List<ChatRoomResponse> user1Deleted = roomService.findAll(user1, PageRequest.of(0, 10));
        List<ChatRoomResponse> user3Deleted = roomService.findAll(user3, PageRequest.of(0, 10));

        ChatRoom chatRoom = chatRoomRepository.findById(user1AndUser3ChatRoomId).get();

        // then
        assertThat(user1Deleted.size()).isEqualTo(0);
        assertThat(user3Deleted.size()).isEqualTo(0);
        assertThat(chatRoom.isActive()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않거나 비활성화 상태인 채팅방을 삭제하면 예외를 발생시킨다.")
    void deleteChatRoom3() {
        // given
        testKrUtils.login(user1);

        // when

        // then
        assertThatThrownBy(() -> roomService.deleteChatRoom(999L, user1))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("채팅방에 참여하지 않은 사용자가 채팅방을 삭제하면 예외를 발생시킨다.")
    void deleteChatRoom4() {
        // given
        testKrUtils.login(user1);

        // when

        // then
        assertThatThrownBy(() -> roomService.deleteChatRoom(user1AndUser3ChatRoomId, user2))
                .isInstanceOf(ChatException.class);
    }
}