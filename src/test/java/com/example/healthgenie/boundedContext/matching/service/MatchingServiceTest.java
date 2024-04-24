package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.exception.CustomException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MatchingServiceTest {

    @Autowired
    MatchingService matchingService;
    @Autowired
    TestKrUtils testKrUtils;

    User user1;
    User trainer1;
    User other1;
    LocalDateTime date;
    Matching matching1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        trainer1 = testKrUtils.createUser("trainer1@test.com", "trainer1", AuthProvider.GOOGLE, Role.TRAINER);
        other1 = testKrUtils.createUser("other1@test.com", "other1", AuthProvider.GOOGLE, Role.USER);
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
    @DisplayName("존재하지 않는 사용자와 매칭 저장 불가능")
    void save_notExistsUser_exception() {
        // given
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        MatchingRequest request = MatchingRequest.builder()
                .userId(999L)
                .date(date)
                .place("헬스장")
                .description("일정 소개")
                .build();

        // when

        // then
        assertThatThrownBy(() -> matchingService.save(trainer1, request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("역할이 올바르지 않다면 매칭 저장 불가능")
    void save_noPermissionRole_exception() {
        // given
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        MatchingRequest request = MatchingRequest.builder()
                .userId(trainer1.getId())
                .date(date)
                .place("헬스장")
                .description("일정 소개")
                .build();

        // when

        // then
        assertThatThrownBy(() -> matchingService.save(user1, request))
                .isInstanceOf(CustomException.class);
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
    @DisplayName("존재하지 않는 매칭 조회 불가능")
    void findById_notExistsMatching_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> matchingService.findById(999L, user1))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("자신의 매칭이 아니라면 조회 불가능")
    void findById_notMine_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> matchingService.findById(matching1.getId(), other1))
                .isInstanceOf(CustomException.class);
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
    void update_by_user() {
        // given

        // when
        Matching updateMatching = matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE);

        // then
        assertThat(updateMatching.getState()).isEqualTo(MatchingState.PARTICIPATE);
    }

    @Test
    @DisplayName("매칭 상태 변경 - TRAINER")
    void update_by_trainer() {
        // given
        matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE);

        // when
        Matching updateMatching = matchingService.update(matching1.getId(), trainer1, MatchingState.PARTICIPATE_ACCEPT);

        // then
        assertThat(updateMatching.getState()).isEqualTo(MatchingState.PARTICIPATE_ACCEPT);
    }

    @Test
    @DisplayName("존재하지 않는 매칭 수정 불가능")
    void update_notExistsMatching_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> matchingService.update(999L, user1, MatchingState.PARTICIPATE))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("일반 회원 역할이 아니라면 참여/취소 요청 불가능")
    void update_notChangeStateByTrainerRole_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> matchingService.update(matching1.getId(), trainer1, MatchingState.PARTICIPATE))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("트레이너 역할이 아니라면 참여/취소 승인 요청 불가능")
    void update_notChangeStateByUserRole_exception() {
        // given
        matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE);

        // when

        // then
        assertThatThrownBy(() -> matchingService.update(matching1.getId(), user1, MatchingState.PARTICIPATE_ACCEPT))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("변경할 상태가 순차적인 상태 변경이 아니라면 수정 불가능")
    void update_wrongChangeOrder_exception() {
        // given
//        matchingService.update(matching1.getId(), user1, MatchingState.CANCEL); // 현재 매칭의 상태를 취소로 변경하지 않고, 취소 승인을 하는 경우

        // when

        // then
        assertThatThrownBy(() -> matchingService.update(matching1.getId(), trainer1, MatchingState.CANCEL_ACCEPT))
                .isInstanceOf(CustomException.class);
    }
}