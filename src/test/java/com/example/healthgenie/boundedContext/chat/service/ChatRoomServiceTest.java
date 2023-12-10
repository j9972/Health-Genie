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
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    TestKrUtils testKrUtils;

    User sender;
    User receiver;
    User other;

    @BeforeEach
    void before() {
        // 사용자 생성
        sender = testKrUtils.createUser("sender1", Role.USER, "sender1@test.com");
        receiver = testKrUtils.createUser("receiver", Role.TRAINER, "receiver1@test.com");
        other = testKrUtils.createUser("other1", Role.EMPTY, "other1@test.com");
    }

    @Test
    @DisplayName("정상적으로 새로운 채팅방을 생성한다.")
    void rightCreateRoom() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        // when
        RoomResponse response = chatRoomService.joinRoom(request);

        // then
        assertThat(response.getSenderId()).isEqualTo(sender.getId());
        assertThat(response.getReceiverId()).isEqualTo(receiver.getId());
        assertThat(response.isSenderOut()).isFalse();
        assertThat(response.isReceiverOut()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 사용자와 채팅방을 생성한다.")
    void notExistReceiver() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), 999L);

        // when

        // then
        assertThatThrownBy(() -> chatRoomService.joinRoom(request))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("자기 자신과 채팅방을 생성한다.")
    void selfChat() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), sender.getId());

        // when
        assertThatThrownBy(() -> chatRoomService.joinRoom(request))
                .isInstanceOf(ChatException.class);

        // then
    }

    @Test
    @DisplayName("정상적으로 기존의 채팅방에 입장한다.")
    void rightAlreadyRoom() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        // when
        RoomResponse response1 = chatRoomService.joinRoom(request);
        RoomResponse response2 = chatRoomService.joinRoom(request);

        // then
        assertThat(response1.getId()).isEqualTo(response2.getId());
    }

    @Test
    @DisplayName("현재 사용자의 모든 채팅방을 조회한다.")
    void getChatRooms() {
        // given
        testKrUtils.login(sender);

        RoomRequest request1 = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
        RoomRequest request2 = testKrUtils.createRoomRequest(sender.getId(), other.getId());

        chatRoomService.joinRoom(request1);
        chatRoomService.joinRoom(request2);

        // when
        List<RoomResponse> rooms = chatRoomService.getRooms();

        // then
        assertThat(rooms.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방에 참여하지 않은 사용자가 채팅방 상세 보기")
    void wrongUserChatDetail() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        RoomResponse response = chatRoomService.joinRoom(request);

        // when
        testKrUtils.login(other);

        // then
        assertThatThrownBy(() -> chatRoomService.getRoomDetail(response.getId()))
                .isInstanceOf(ChatException.class);
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 상세 보기")
    void notExistChatRoom() {
        // given
        testKrUtils.login(sender);

        // when
        assertThatThrownBy(() -> chatRoomService.getRoomDetail(999L))
                .isInstanceOf(ChatException.class);

        // then
    }

    @Test
    @DisplayName("한명만 채팅방 나가기")
    void oneExitChatRoom() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        RoomResponse createResponse = chatRoomService.joinRoom(request);

        // when
        chatRoomService.deleteRoom(createResponse.getId());

        testKrUtils.login(receiver);

        // then
        RoomResponse afterSenderOutResponse = chatRoomService.joinRoom(request);

        assertThat(afterSenderOutResponse.isSenderOut()).isTrue();
        assertThat(afterSenderOutResponse.isReceiverOut()).isFalse();
    }

    @Test
    @DisplayName("양쪽 모두 채팅방 나가기")
    void allExitChatRoom() {
        // given
        testKrUtils.login(sender);

        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        RoomResponse response = chatRoomService.joinRoom(request);

        // when
        chatRoomService.deleteRoom(response.getId());

        testKrUtils.login(receiver);
        chatRoomService.deleteRoom(response.getId());

        // then
        ChatRoom chatRoom = chatRoomRepository.findById(response.getId()).orElse(null);
        assertThat(chatRoom).isNull();
    }
}