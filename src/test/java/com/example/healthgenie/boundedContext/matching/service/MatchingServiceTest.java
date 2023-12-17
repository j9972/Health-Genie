package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    User user1;
    User trainer1;
    String ptDate;
    String ptTime;
    Matching matching1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1", Role.USER, "user1@test.com");
        trainer1 = testKrUtils.createUser("trainer1", Role.TRAINER, "trainer1@test.com");
        // 2023년 12월 12일 16시 30분
        ptDate = "2023.12.12";
        ptTime = "16:30:00";
        matching1 = testKrUtils.createMatching(
                DateUtils.toLocalDateTime(ptDate, ptTime),
                "기본 매칭 장소",
                "기본 매칭 내용",
                user1,
                trainer1);
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
        assertThat(savedMatching.getUserId()).isEqualTo(user1.getId());
        assertThat(savedMatching.getTrainerId()).isEqualTo(trainer1.getId());
    }

    @Test
    @DisplayName("매칭 상세 조회하기")
    void findOne() {
        // given
        testKrUtils.login(user1);

        List<Matching> matchings = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            LocalDateTime date = LocalDateTime.of(2099, 1, 1, i*2, 30);
            Matching savedMatching = testKrUtils.createMatching(date, "테스트 체육관" + i, "테스트 PT 내용" + i, user1, trainer1);
            matchings.add(savedMatching);
        }

        // when
        MatchingResponse response = matchingService.findOne(matchings.get(0).getId());

        // then
        assertThat(response.getId()).isEqualTo(matchings.get(0).getId());
        assertThat(response.getDate()).isEqualTo("2099.01.01");
        assertThat(response.getTime()).isEqualTo("02:30:00");
        assertThat(response.getPlace()).isEqualTo("테스트 체육관1");
        assertThat(response.getDescription()).isEqualTo("테스트 PT 내용1");
        assertThat(response.getUserId()).isEqualTo(user1.getId());
        assertThat(response.getTrainerId()).isEqualTo(trainer1.getId());
    }

    @Test
    @DisplayName("해당 날짜의 매칭 조회하기")
    void findByDateAndNicknames() {
        // given
        testKrUtils.login(user1);

        for(int i=1; i<=10; i++) {
            LocalDateTime date = LocalDateTime.of(2099, 1, 1, i*2, 30);
            testKrUtils.createMatching(
                    date,
                    "테스트 체육관" + i,
                    "테스트 PT 내용" + i,
                    user1,
                    trainer1
            );
        }

        // when
        MatchingCondition condition = testKrUtils.createMatchingCondition("2099.01.01", user1.getId(), trainer1.getId());

        List<MatchingResponse> responses = matchingService.findAll(condition);

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