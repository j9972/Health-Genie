package com.example.healthgenie.boundedContext.chat.service;


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
    void createChatRoom() {
        // given
        testKrUtils.login(sender);

        ChatRoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());

        // when
        Long chatRoomId = roomService.createChatRoom(request);

        ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        List<ChatRoomUser> chatRoomUsers = findChatRoom.getChatRoomUsers();

        // then
        assertThat(chatRoomUsers).extracting(ChatRoomUser::getUser).contains(sender, receiver);
    }

    @Test
    @DisplayName("정상적으로 채팅방 목록을 조회한다.")
    void getChatRooms() {
        // given
        testKrUtils.login(sender);

        ChatRoomRequest createRequest1 = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
        ChatRoomRequest createRequest2 = testKrUtils.createRoomRequest(sender.getId(), other.getId());

        roomService.createChatRoom(createRequest1);
        roomService.createChatRoom(createRequest2);

        // when
        List<ChatRoomResponse> responses = roomService.getChatRooms(sender, PageRequest.of(0, 10));

        // then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("정상적으로 채팅방 내역을 조회한다.")
    void getMessages() {
        // given
        testKrUtils.login(sender);

        ChatRoomRequest createRequest = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
        Long roomId = roomService.createChatRoom(createRequest);

        // when
        List<GetMessageResponse> responses = roomService.getMessages(roomId, sender);

        // then
        assertThat(responses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("정상적으로 채팅방을 삭제한다.")
    void deleteChatRoom() {
        // given
        testKrUtils.login(sender);

        ChatRoomRequest createRequest = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
        Long roomId = roomService.createChatRoom(createRequest);

        // when
        roomService.deleteChatRoom(roomId, sender);

        List<ChatRoomResponse> deleted = roomService.getChatRooms(sender, PageRequest.of(0, 10));
        List<ChatRoomResponse> notDeleted = roomService.getChatRooms(receiver, PageRequest.of(0, 10));

        // then
        assertThat(deleted.size()).isEqualTo(0);
        assertThat(notDeleted.size()).isEqualTo(1);
    }

//    @Test
//    @DisplayName("존재하지 않는 사용자와 채팅방을 생성한다.")
//    void notExistReceiver() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), 999L);
//
//        // when
//
//        // then
//        assertThatThrownBy(() -> roomService.joinRoom(request))
//                .isInstanceOf(CommonException.class);
//    }
//
//    @Test
//    @DisplayName("자기 자신과 채팅방을 생성한다.")
//    void selfChat() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), sender.getId());
//
//        // when
//        assertThatThrownBy(() -> roomService.joinRoom(request))
//                .isInstanceOf(ChatException.class);
//
//        // then
//    }
//
//    @Test
//    @DisplayName("정상적으로 기존의 채팅방에 입장한다.")
//    void rightAlreadyRoom() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
//
//        // when
//        RoomResponse response1 = roomService.joinRoom(request);
//        RoomResponse response2 = roomService.joinRoom(request);
//
//        // then
//        assertThat(response1.getId()).isEqualTo(response2.getId());
//    }
//
//    @Test
//    @DisplayName("현재 사용자의 모든 채팅방을 조회한다.")
//    void getChatRooms() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request1 = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
//        RoomRequest request2 = testKrUtils.createRoomRequest(sender.getId(), other.getId());
//
//        roomService.joinRoom(request1);
//        roomService.joinRoom(request2);
//
//        // when
//        List<RoomResponse> rooms = roomService.getRooms();
//
//        // then
//        assertThat(rooms.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("채팅방에 참여하지 않은 사용자가 채팅방 상세 보기")
//    void wrongUserChatDetail() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
//
//        RoomResponse response = roomService.joinRoom(request);
//
//        // when
//        testKrUtils.login(other);
//
//        // then
//        assertThatThrownBy(() -> roomService.getRoomDetail(response.getId()))
//                .isInstanceOf(ChatException.class);
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 채팅방 상세 보기")
//    void notExistChatRoom() {
//        // given
//        testKrUtils.login(sender);
//
//        // when
//        assertThatThrownBy(() -> roomService.getRoomDetail(999L))
//                .isInstanceOf(ChatException.class);
//
//        // then
//    }
//
//    @Test
//    @DisplayName("한명만 채팅방 나가기")
//    void oneExitChatRoom() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
//
//        RoomResponse createResponse = roomService.joinRoom(request);
//
//        // when
//        roomService.deleteRoom(createResponse.getId());
//
//        testKrUtils.login(receiver);
//
//        // then
//        RoomResponse afterSenderOutResponse = roomService.joinRoom(request);
//
//        assertThat(afterSenderOutResponse.isSenderOut()).isTrue();
//        assertThat(afterSenderOutResponse.isReceiverOut()).isFalse();
//    }
//
//    @Test
//    @DisplayName("양쪽 모두 채팅방 나가기")
//    void allExitChatRoom() {
//        // given
//        testKrUtils.login(sender);
//
//        RoomRequest request = testKrUtils.createRoomRequest(sender.getId(), receiver.getId());
//
//        RoomResponse response = roomService.joinRoom(request);
//
//        // when
//        roomService.deleteRoom(response.getId());
//
//        testKrUtils.login(receiver);
//        roomService.deleteRoom(response.getId());
//
//        // then
//        ChatRoom chatRoom = chatRoomRepository.findById(response.getId()).orElse(null);
//        assertThat(chatRoom).isNull();
//    }
}