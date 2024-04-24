package com.example.healthgenie.boundedContext.chat.service;


import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    RoomService roomService;
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
    @DisplayName("채팅방 생성 또는 기존 채팅방 활성화")
    void saveOrActive() {
        // given
        RoomRequest request1 = RoomRequest.builder().anotherUserId(user2.getId()).build(); // 기존 채팅방 조회
        RoomRequest request2 = RoomRequest.builder().anotherUserId(user3.getId()).build(); // 새 채팅방 생성

        // when
        Room room1 = roomService.saveOrActive(user1, request1);
        Room room2 = roomService.saveOrActive(user1, request2);

        // then
        assertThat(room1.getRoomUsers()).extracting(RoomUser::getUser).contains(user1, user2);
        assertThat(room2.getRoomUsers()).extracting(RoomUser::getUser).contains(user1, user3);
    }

    @Test
    @DisplayName("존재하지 않는 사용자와 채팅방 생성 불가능")
    void saveOrActive_notExistsUser_exception() {
        // given
        RoomRequest request = RoomRequest.builder().anotherUserId(999L).build();

        // when

        // then
        assertThatThrownBy(() -> roomService.saveOrActive(user1, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("채팅방 단건 조회")
    void findById() {
        // given

        // when
        Room findRoom = roomService.findById(user1AndUser2Room.getId());

        // then
        assertThat(findRoom.getId()).isEqualTo(user1AndUser2Room.getId());
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 조회 불가능")
    void findById_notExistsRoom_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> roomService.findById(999L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("채팅방 전체 조회")
    void findAll() {
        // given
        RoomRequest request = RoomRequest.builder().anotherUserId(user3.getId()).build();
        Room user1AndUser3Room = roomService.saveOrActive(user1, request);

        Long start = user1AndUser2Room.getId();
        Long end = user1AndUser3Room.getId();

        // when
        Slice<RoomQueryResponse> findRooms1 = roomService.findAll(user1, null, PageRequest.of(0, 1));
        Slice<RoomQueryResponse> findRooms2 = roomService.findAll(user1, (start+end)/2+1, PageRequest.of(0, 1));

        // then
        assertThat(findRooms1.isLast()).isFalse();
        assertThat(findRooms1.getContent().size()).isEqualTo(1);
        assertThat(findRooms2.isLast()).isTrue();
        assertThat(findRooms2.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("채팅방 비활성화")
    void inactive() {
        // given

        // when
        roomService.inactive(user1AndUser2Room.getId(), user1);

        // then
        List<RoomQueryResponse> rooms = roomService.findAll(user1, null, PageRequest.ofSize(5)).getContent();

        assertThat(rooms.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 비활성화 불가능")
    void inactive_notExistsRoom_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> roomService.inactive(999L, user1))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("채팅방에 참여하지 않는 사용자는 비활성화 불가능")
    void inactive_notParticipateUser_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> roomService.inactive(user1AndUser2Room.getId(), user3))
                .isInstanceOf(CustomException.class);
    }
}