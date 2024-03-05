package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.enums.MatchingState;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MatchingServiceTest {

    @Autowired
    MatchingService matchingService;
    @Autowired
    TestKrUtils testKrUtils;

    User user1;
    User trainer1;
    LocalDateTime date;
    Matching matching1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        trainer1 = testKrUtils.createUser("trainer1@test.com", "trainer1", AuthProvider.GOOGLE, Role.TRAINER);
        date = LocalDateTime.of(2024, 12, 31, 12, 30, 0);
        matching1 = testKrUtils.createMatching(trainer1, user1.getId(), date, "OO대학교", "PT 일정 제안");
    }

    @Test
    @DisplayName("매칭 저장")
    void save() {
        // given
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        MatchingRequest request = MatchingRequest.builder()
                .userId(user1.getId())
                .date(date)
                .place("헬스장")
                .description("일정 소개")
                .build();

        // when
        Matching saveMatching = matchingService.save(trainer1, request);

        // then
        assertThat(saveMatching.getDate()).isEqualTo(date);
        assertThat(saveMatching.getPlace()).isEqualTo("헬스장");
        assertThat(saveMatching.getDescription()).isEqualTo("일정 소개");
        assertThat(saveMatching.getMatchingUsers()).extracting(MatchingUser::getUser).contains(user1, trainer1);
    }

    @Test
    @DisplayName("매칭 단건 조회 - id")
    void findById() {
        // given

        // when
        Matching findMatching = matchingService.findById(matching1.getId(), user1);

        // then
        assertThat(findMatching.getId()).isEqualTo(matching1.getId());
        assertThat(findMatching.getDate()).isEqualTo(matching1.getDate());
        assertThat(findMatching.getPlace()).isEqualTo(matching1.getPlace());
        assertThat(findMatching.getDescription()).isEqualTo(matching1.getDescription());
        assertThat(findMatching.getState()).isEqualTo(matching1.getState());
        assertThat(findMatching.getMatchingUsers()).extracting(MatchingUser::getUser).contains(trainer1, user1);
    }

    @Test
    @DisplayName("매칭 전체 조회 - 날짜")
    void findAll() {
        // given
        for(int i=1; i<30; i++) {
            if(i == 10) {
                for(int j=1; j<=5; j++) {
                    testKrUtils.createMatching(trainer1, user1.getId(), LocalDateTime.of(2024, 12, i, j*2, 30, 0), "장소", "내용");
                }
                continue;
            }
            testKrUtils.createMatching(trainer1, user1.getId(), LocalDateTime.of(2024, 12, i, 10, 30, 0), "장소", "내용");
        }

        MatchingCondition condition = MatchingCondition.builder().date(LocalDateTime.of(2024, 12, 10, 0, 0, 0)).build();

        // when
        List<Matching> findMatchings = matchingService.findAll(trainer1, condition);

        // then
        assertThat(findMatchings.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("매칭 상태 변경 - USER")
    void updateUser() {
        // given

        // when
        Matching updateMatching = matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE);

        // then
        assertThat(updateMatching.getState()).isEqualTo(MatchingState.PARTICIPATE);
    }

    @Test
    @DisplayName("매칭 상태 변경 - TRAINER")
    void updateTrainer() {
        // given
        matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE);

        // when
        Matching updateMatching = matchingService.update(matching1.getId(), trainer1, MatchingState.PARTICIPATE_ACCEPT);

        // then
        assertThat(updateMatching.getState()).isEqualTo(MatchingState.PARTICIPATE_ACCEPT);
    }
}