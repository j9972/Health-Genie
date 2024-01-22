package com.example.healthgenie.boundedContext.chat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChatRedisRepositoryTest {

    @Autowired
    ChatRedisRepository chatRedisRepository;

    @Test
    void putTest() {
    }

    @Test
    void getTest() {

    }

    @Test
    void delTest() {

    }

    @Test
    void userEnterRoomInfo() {
    }

    @Test
    void existChatRoomUserInfo() {
    }

    @Test
    void existUserRoomInfo() {
    }

    @Test
    void getUserEnterRoomId() {
    }

    @Test
    void exitUserEnterRoomId() {
    }

    @Test
    void initChatRoomMessageInfo() {
    }

    @Test
    void addChatRoomMessageCount() {
    }

    @Test
    void getChatRoomMessageCount() {
    }

    @Test
    void saveMyInfo() {
    }

    @Test
    void existMyInfo() {
    }

    @Test
    void getMyInfo() {
    }

    @Test
    void deleteMyInfo() {
    }
}