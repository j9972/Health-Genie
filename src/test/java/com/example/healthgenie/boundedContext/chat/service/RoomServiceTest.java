package com.example.healthgenie.boundedContext.chat.service;


import com.example.healthgenie.boundedContext.chat.dto.RoomQueryResponse;
import com.example.healthgenie.boundedContext.chat.dto.RoomRequest;
import com.example.healthgenie.boundedContext.chat.entity.Room;
import com.example.healthgenie.boundedContext.chat.entity.RoomUser;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
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
    @DisplayName("채팅방 단건 조회")
    void findById() {
        // given

        // when
        Room findRoom = roomService.findById(user1AndUser2Room.getId());

        // then
        assertThat(findRoom.getId()).isEqualTo(user1AndUser2Room.getId());
    }

    @Test
    @DisplayName("채팅방 전체 조회")
    void findAll() {
        // given
        RoomRequest request = RoomRequest.builder().anotherUserId(user3.getId()).build();
        roomService.saveOrActive(user1, request);

        // when
        // TODO : findAll() 메서드 무한 스크롤 구현 후 수정 해야함. (현재, lastId와 Pageable 무시)
        List<RoomQueryResponse> rooms = roomService.findAll(user1, 0L, PageRequest.of(0, 1));

        // then
        assertThat(rooms.size()).isEqualTo(2);
    }

//    @Test
//    @DisplayName("채팅방 비활성화(=삭제)")
//    void inactive() {
//        // given
//
//        // when
//        roomService.inactive(user1AndUser2Room.getId(), user1);
//
//        // then
//        assertThat(roomService.findAll(user1, 0L, PageRequest.of(0, 1)).size()).isEqualTo(0);
//    }
}