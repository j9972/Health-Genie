package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    EntityManager em;

    User user1;
    User trainer1;
    LocalDateTime ptDate;
    Matching matching1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1", Role.USER, "user1@test.com");
        trainer1 = testKrUtils.createUser("trainer1", Role.TRAINER, "trainer1@test.com");
        // 2023년 12월 12일 16시 30분
        ptDate = LocalDateTime.of(2023, 12, 12, 16, 30);
        matching1 = testKrUtils.createMatching(ptDate, "기본 매칭 장소", "기본 매칭 내용", user1, trainer1);
    }

    @Test
    @DisplayName("정상적으로 매칭 생성하기")
    void save() {
        // given
        testKrUtils.login(user1);

        MatchingRequest request =
                testKrUtils.createMatchingRequest(ptDate, "00대학교 체육관", "기본 PT 입니다.", user1, trainer1);

        // when
        MatchingResponse savedMatching = matchingService.save(request);

        // then
        assertThat(savedMatching.getDate()).isEqualTo(ptDate);
        assertThat(savedMatching.getUserNickname()).isEqualTo(user1.getNickname());
        assertThat(savedMatching.getTrainerNickname()).isEqualTo(trainer1.getNickname());
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
        LocalDateTime date = LocalDateTime.of(2099, 1, 1, 0, 0);
        MatchingRequest findRequest = testKrUtils.createMatchingRequest(date, user1, trainer1);

        List<MatchingResponse> responses = matchingService.findByDateAndNicknames(findRequest);

        // then
        assertThat(responses.size()).isEqualTo(10);
        assertThat(responses).isSortedAccordingTo(Comparator.comparing(MatchingResponse::getDate));
    }

    @Test
    @DisplayName("정상적으로 회원이 PT를 참석하기")
    void participate() {
        // given
        testKrUtils.login(user1);

        MatchingRequest participateRequest = MatchingRequest.builder()
                .date(ptDate)
                .userNickname(user1.getNickname())
                .trainerNickname(trainer1.getNickname())
                .build();

        // when
        Long id = matchingService.participate(participateRequest);

        Matching matching = matchingRepository.findById(id).get();

        // then
        assertThat(matching.getParticipateState()).isEqualTo(MatchingState.PARTICIPATE);
    }

    @Test
    @DisplayName("정상적으로 회원이 PT를 취소하기")
    void cancel() {
        // given
        testKrUtils.login(user1);

        MatchingRequest cancelRequest = MatchingRequest.builder()
                .date(ptDate)
                .userNickname(user1.getNickname())
                .trainerNickname(trainer1.getNickname())
                .build();

        // when
        Long id = matchingService.cancel(cancelRequest);

        Matching matching = matchingRepository.findById(id).get();

        // then
        assertThat(matching.getParticipateState()).isEqualTo(MatchingState.CANCEL);
    }

    @Test
    @DisplayName("정상적으로 트레이너가 PT 참석을 수락하기")
    void participateAccept() {
        // given
        testKrUtils.login(trainer1);

        MatchingRequest cancelRequest = MatchingRequest.builder()
                .date(ptDate)
                .userNickname(user1.getNickname())
                .trainerNickname(trainer1.getNickname())
                .build();

        // when
        Long id = matchingService.participateAccept(cancelRequest);

        Matching matching = matchingRepository.findById(id).get();

        // then
        assertThat(matching.getAcceptState()).isEqualTo(MatchingState.PARTICIPATE_ACCEPT);
    }

    @Test
    @DisplayName("정상적으로 트레이너가 PT 취소를 수락하기 - 최종 매칭 취소")
    void breakup() {
        // given
        testKrUtils.login(user1);

        MatchingRequest request = MatchingRequest.builder()
                .date(ptDate)
                .userNickname(user1.getNickname())
                .trainerNickname(trainer1.getNickname())
                .build();

        matchingService.cancel(request);

        // when
        testKrUtils.login(trainer1);

        Long id = matchingService.breakup(request);

        // then
        Optional<Matching> opMatching = matchingRepository.findById(id);

        assertThat(opMatching.isEmpty()).isTrue();
    }
}