package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MatchingServiceTest {

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    MatchingService matchingService;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    MatchingUserRepository matchingUserRepository;

    User user1;
    User trainer1;
    String ptDate;
    String ptTime;
    MatchingUser matchingUser1;
    MatchingUser matchingUser2;
    Matching matching1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1", Role.USER, "user1@test.com");
        trainer1 = testKrUtils.createUser("trainer1", Role.TRAINER, "trainer1@test.com");
        // 2023년 12월 12일 16시 30분
        ptDate = "2023.12.12";
        ptTime = "16:30:00";
        matching1 = testKrUtils.createMatching(
                user1.getId(), trainer1.getId(),
                ptDate, ptTime,
                "기본 매칭 장소", "기본 매칭 내용"
        );
        matchingUser1 = matching1.getMatchingUsers().get(0);
        matchingUser2 = matching1.getMatchingUsers().get(1);
    }

    @Test
    @DisplayName("정상적으로 매칭 생성하기")
    void save() {
        // given
        testKrUtils.login(user1);

        MatchingRequest request =
                testKrUtils.createMatchingRequest(ptDate, ptTime, "00대학교 체육관", "기본 PT 입니다.", user1.getId(), trainer1.getId());

        // when
        MatchingResponse savedMatching =
                matchingService.save(request.getUserId(), request.getTrainerId(), request.getDate(), request.getTime(), request.getPlace(), request.getDescription());

        // then
        assertThat(savedMatching.getDate()).isEqualTo(ptDate);
        assertThat(savedMatching.getTime()).isEqualTo(ptTime);
        assertThat(savedMatching.getMatchingUsers()).containsExactly(user1.getId(), trainer1.getId());
    }

    @Test
    @DisplayName("정상적으로 트레이너가 PT 참석을 수락하기")
    void participateAccept() {
        // given
        testKrUtils.login(user1);
        matchingService.update(matching1.getId(), MatchingState.PARTICIPATE);

        // when
        testKrUtils.login(trainer1);

        MatchingResponse response = matchingService.update(matching1.getId(), MatchingState.PARTICIPATE_ACCEPT);

        // then
        assertThat(response.getState()).isEqualTo(MatchingState.PARTICIPATE_ACCEPT);
    }

    @Test
    @DisplayName("매칭 상세 조회하기")
    void findOne() {
        // given
        testKrUtils.login(user1);

        List<Matching> matchings = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            String date = "2099.01.01";
            String time = i*2 + ":00:00";
            if(time.length() == 7) {
                time = 0 + time;
            }
            Matching savedMatching = testKrUtils.createMatching(user1.getId(), trainer1.getId(), date, time, "테스트 체육관" + i, "테스트 PT 내용" + i);
            matchings.add(savedMatching);
        }

        // when
        MatchingResponse response = matchingService.findOne(matchings.get(0).getId());

        // then
        assertThat(response.getId()).isEqualTo(matchings.get(0).getId());
        assertThat(response.getDate()).isEqualTo("2099.01.01");
        assertThat(response.getTime()).isEqualTo("02:00:00");
        assertThat(response.getPlace()).isEqualTo("테스트 체육관1");
        assertThat(response.getDescription()).isEqualTo("테스트 PT 내용1");
        assertThat(response.getMatchingUsers()).containsExactly(user1.getId(), trainer1.getId());
    }

    @Test
    @DisplayName("해당 날짜의 매칭 조회하기")
    void findByDateAndNicknames() {
        // given
        testKrUtils.login(user1);

        for(int i=1; i<=10; i++) {
            String date = "2099.01.01";
            String time = i*2 + ":00:00";
            if(time.length() == 7) {
                time = 0 + time;
            }
            testKrUtils.createMatching(
                    user1.getId(),
                    trainer1.getId(),
                    date,
                    time,
                    "테스트 체육관" + i,
                    "테스트 PT 내용" + i
            );
        }

        // when
        MatchingCondition condition = testKrUtils.createMatchingCondition("2099.01.01");

        List<MatchingResponse> responses = matchingService.findAll(user1, condition);

        // then
        assertThat(responses.size()).isEqualTo(10);
        assertThat(responses).isSortedAccordingTo(Comparator.comparing(MatchingResponse::getDate));
    }

    @Test
    @DisplayName("정상적으로 회원이 PT를 참석하기")
    void participate() {
        // given
        testKrUtils.login(user1);

        // when
        MatchingResponse response = matchingService.update(matching1.getId(), MatchingState.PARTICIPATE);

        // then
        assertThat(response.getState()).isEqualTo(MatchingState.PARTICIPATE);
    }

    @Test
    @DisplayName("정상적으로 회원이 PT를 취소하기")
    void cancel() {
        // given
        testKrUtils.login(user1);

        // when
        MatchingResponse response = matchingService.update(matching1.getId(), MatchingState.CANCEL);

        // then
        assertThat(response.getState()).isEqualTo(MatchingState.CANCEL);
    }

    @Test
    @DisplayName("정상적으로 트레이너가 PT 취소를 수락하기")
    void breakup() {
        // given
        testKrUtils.login(user1);
        matchingService.update(matching1.getId(), MatchingState.CANCEL);

        // when
        testKrUtils.login(trainer1);

        MatchingResponse response = matchingService.update(matching1.getId(), MatchingState.CANCEL_ACCEPT);

        // then
        assertThat(response.getState()).isEqualTo(MatchingState.CANCEL_ACCEPT);
    }
}