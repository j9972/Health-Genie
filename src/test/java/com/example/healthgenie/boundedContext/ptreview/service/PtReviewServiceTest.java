package com.example.healthgenie.boundedContext.ptreview.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PtReviewServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    PtReviewService reviewService;

    User user;
    User user2;
    User user3;
    User user4;
    PtReview review;

    Matching matching;


    @BeforeEach
    void before() {
        LocalDateTime date = LocalDateTime.of(2023, 12, 5, 14, 30, 0);

        user = testSyUtils.createUser("test1", Role.USER,"jh485200@gmail.com");
        user2 = testSyUtils.createUser("test2", Role.TRAINER,"test@test.com");
        user3 = testSyUtils.createUser("test3", Role.USER,"test3@gmail.com");
        user4 = testSyUtils.createUser("test4", Role.TRAINER,"test4@test.com");

        matching = testKrUtils.createMatching(date, "gym", "test",  user, user2);
        review = testSyUtils.createReview("test review","stop",4.5, user3,user4);
    }

    @Test
    @DisplayName("회원이 리뷰 작성 성공")
    void addPtReview() {
        // given
        testSyUtils.login(user);

        PtReviewRequestDto dto = testSyUtils.createReviewDto("test", "test", 4.5, "test1", "test2");

        // when
        PtReviewResponseDto response = reviewService.addPtReview(dto);

        // then
        assertThat(response.getStopReason()).isEqualTo("test");
        assertThat(response.getContent()).isEqualTo("test");
        assertThat(response.getReviewScore()).isEqualTo(4.5);
        assertThat(response.getUserNickName()).isEqualTo("test1");
        assertThat(response.getTrainerNickName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("회원이 아닌 트레이너면 리뷰 작성 실패")
    void failAddPtReview() {
        // given
        testSyUtils.login(user2);

        PtReviewRequestDto dto = testSyUtils.createReviewDto("test", "test", 4.5, "test1", "test2");
        // when

        // then
        assertThatThrownBy(() -> {
            if(!user2.getRole().equals(Role.USER)) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("로그인 안하면 리뷰 작성 실패")
    void notLoginAddPtReview() {
        // given

        PtReviewRequestDto dto = testSyUtils.createReviewDto("test", "test", 4.5, "test1", "test2");
        // when

        // then
        assertThatThrownBy(() -> reviewService.addPtReview(dto))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("매칭 기록없이 리뷰 작성 실패")
    void noMatchingHistoryAddPtReview() {
        // given
        testSyUtils.login(user3);

        PtReviewRequestDto dto = testSyUtils.createReviewDto("test", "test", 4.5, "test3", "test4");
        // when

        // then
        assertThatThrownBy(() -> reviewService.addPtReview(dto))
                .isInstanceOf(MatchingException.class);
    }


    @Test
    @DisplayName("리뷰 조회 성공")
    void getPtReview() {
        // given
        // review 작성한 사람 아니여도 조회 가능
        testSyUtils.login(user);

        // when
        PtReviewResponseDto response = reviewService.getPtReview(review.getId());

        // then
        assertThat(response.getStopReason()).isEqualTo("stop");
        assertThat(response.getContent()).isEqualTo("test review");
        assertThat(response.getReviewScore()).isEqualTo(4.5);
        assertThat(response.getUserNickName()).isEqualTo("test3");
        assertThat(response.getTrainerNickName()).isEqualTo("test4");
    }

    @Test
    @DisplayName("존재하지 않는 리뷰 조회 실패")
    void notExistPtReview() {
        // given
        testSyUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> reviewService.getPtReview(999L))
                .isInstanceOf(PtReviewException.class);
    }

    @Test
    @DisplayName("해당 트레이너 리뷰 전체 조회 성공")
    void getAllTrainerReview() {
        // given
        testSyUtils.login(user);
        Long trainerId = review.getTrainer().getId();

        // when
        Page<PtReviewResponseDto> response = reviewService.getAllTrainerReview(trainerId,0, 5);

        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("test review");
        assertThat(response.getContent().get(0).getReviewScore()).isEqualTo(4.5);
        assertThat(response.getContent().get(0).getStopReason()).isEqualTo("stop");
        assertThat(response.getContent().get(0).getUserNickName()).isEqualTo(user3.getNickname());
        assertThat(response.getContent().get(0).getTrainerNickName()).isEqualTo(user4.getNickname());
    }

    @Test
    @DisplayName("내가 작성한 리뷰 전체 조회 성공")
    void getAllReview() {
        // given
        testSyUtils.login(user3);
        Long userId = review.getMember().getId();

        // when
        Page<PtReviewResponseDto> response = reviewService.getAllReview(userId, 0, 5);


        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("test review");
        assertThat(response.getContent().get(0).getReviewScore()).isEqualTo(4.5);
        assertThat(response.getContent().get(0).getStopReason()).isEqualTo("stop");
        assertThat(response.getContent().get(0).getUserNickName()).isEqualTo(user3.getNickname());
        assertThat(response.getContent().get(0).getTrainerNickName()).isEqualTo(user4.getNickname());
    }
// review user3, user4
    @Test
    @DisplayName("리뷰 수정 성공")
    void updateReview() {
        // given
        testSyUtils.login(user3);

        // when
        PtReviewRequestDto dto = testSyUtils.createReviewDto("update", "update", 4.0, "test3", "test4");

        // then
        PtReviewResponseDto saved = reviewService.updateReview(dto, review.getId());

        assertThat(saved.getContent()).isEqualTo("update");
        assertThat(saved.getStopReason()).isEqualTo("update");
        assertThat(saved.getReviewScore()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 리뷰 수정 실패")
    void notLoginUpdateReview() {
        // given


        // when
        PtReviewRequestDto dto = testSyUtils.createReviewDto("update", "update", 4.0, "test3", "test4");

        // then
        assertThatThrownBy(() -> reviewService.updateReview(dto, review.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("다른 사람이 작성한 리뷰 수정 실패")
    void failOtherReviewUpdate() {
        // given
        testSyUtils.login(user);

        // when
        PtReviewRequestDto dto = testSyUtils.createReviewDto("update", "update", 4.0, "test3", "test4");

        // then
        assertThatThrownBy(() -> reviewService.updateReview(dto, review.getId()))
                .isInstanceOf(PtReviewException.class);
    }

    @Test
    @DisplayName("트레이너가 리뷰 수정 실패")
    void trainerFailReviewUpdate() {
        // given
        testSyUtils.login(user4);

        // when
        PtReviewRequestDto dto = testSyUtils.createReviewDto("update", "update", 4.0, "test3", "test4");

        // then
        assertThatThrownBy(() -> {
            if(!user2.getRole().equals(Role.USER)) {
                throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
            }
        }).isInstanceOf(PtReviewException.class);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deletePtReview() {
        // given
        testSyUtils.login(user3);

        // when

        // then
        String response = reviewService.deletePtReview(review.getId());
        assertThat(response).isEqualTo("후기가 삭제 되었습니다.");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 리뷰 삭제 실패")
    void notLoginDeletePtReview() {
        // given

        // when

        // then
        assertThatThrownBy(() -> reviewService.deletePtReview(review.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("트레이가 리뷰 삭제 실패")
    void failTrainerDeletePtReview() {
        // given
        testSyUtils.login(user4);

        // when

        // then
        assertThatThrownBy(() -> {
            if(!user2.getRole().equals(Role.USER)) {
                throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
            }
        }).isInstanceOf(PtReviewException.class);
    }

    @Test
    @DisplayName("다른 사람 리뷰 삭제 실패")
    void failOtherDeletePtReview() {
        // given
        testSyUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> reviewService.deletePtReview(review.getId()))
                .isInstanceOf(PtReviewException.class);
    }

    @Test
    @DisplayName("리뷰 검색 성공")
    void findAll() {
        // given
        testSyUtils.login(user);

        // when
        String keyword = "review";
        List<PtReviewResponseDto> response = reviewService.findAll(keyword);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response).isSortedAccordingTo(Comparator.comparingLong(PtReviewResponseDto::getId).reversed());
        assertThat(response).extracting(PtReviewResponseDto::getContent).doesNotContain("test");
    }

    @Test
    @DisplayName("리뷰 날짜 필터링 성공")
    void findAllByDate() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2023, 12, 8);

        // when
        List<PtReviewResponseDto> reviews = reviewService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(reviews).isNotNull();
        assertThat(reviews).isNotEmpty();
    }
}