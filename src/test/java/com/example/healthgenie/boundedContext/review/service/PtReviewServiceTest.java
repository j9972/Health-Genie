package com.example.healthgenie.boundedContext.review.service;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.DUPLICATED;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;
import static com.example.healthgenie.base.exception.ErrorCode.UNKNOWN_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.review.dto.PtReviewDeleteResponseDto;
import com.example.healthgenie.boundedContext.review.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.review.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.review.dto.PtReviewUpdateRequest;
import com.example.healthgenie.boundedContext.review.entity.PtReview;
import com.example.healthgenie.boundedContext.review.repository.PtReviewQueryRepository;
import com.example.healthgenie.boundedContext.review.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.service.UserService;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PtReviewServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    PtReviewService reviewService;

    @Autowired
    MatchingUserRepository matchingUserRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    PtReviewRepository ptReviewRepository;

    @Autowired
    UserService userService;

    @Autowired
    PtReviewQueryRepository ptReviewQueryRepository;

    User user;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
    PtReview review;

    Matching matching;
    Matching matching2;


    @BeforeEach
    void before() {
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 1, 11, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2030, 1, 1, 11, 0, 0);

        user = testKrUtils.createUser("jh485200@gmail.com", "test1", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test@test.com", "test2", AuthProvider.EMPTY, Role.TRAINER);
        user3 = testKrUtils.createUser("test3@gamil.com", "test3", AuthProvider.EMPTY, Role.USER);
        user4 = testKrUtils.createUser("test4@test.com", "test4", AuthProvider.EMPTY, Role.TRAINER);
        user5 = testKrUtils.createUser("test5@gmail.com", "test5", AuthProvider.EMPTY, Role.USER);
        user6 = testKrUtils.createUser("test6@test.com", "test6", AuthProvider.EMPTY, Role.TRAINER);

        userService.update(user, null, "test1", null, null, null, null, null);
        userService.update(user2, null, "test2", null, null, null, null, null);
        userService.update(user3, null, "test3", null, null, null, null, null);
        userService.update(user4, null, "test4", null, null, null, null, null);

        matching = testKrUtils.createMatching(user2, user.getId(), date1, "체육관", "pt내용");
        matching2 = testKrUtils.createMatching(user6, user5.getId(), date2, "체육관", "pt내용");

        review = testSyUtils.createReview("test review", "stop", 4.5, user3, user4);
    }

    @Test
    @DisplayName("회원이 리뷰 작성 성공")
    void add_review() {
        // given
        testKrUtils.login(user);

        PtReviewRequestDto dto = testSyUtils.createReviewDto("test", "test", 4.5, user.getId(), user2.getId());

        // when
        PtReviewResponseDto response = reviewService.addPtReview(dto, user);

        // then
        assertThat(response.getStopReason()).isEqualTo("test");
        assertThat(response.getContent()).isEqualTo("test");
        assertThat(response.getReviewScore()).isEqualTo(4.5);
        assertThat(response.getUserName()).isEqualTo("test1");
        assertThat(response.getTrainerName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("리뷰 작성 날짜가 매칭 이전이여서 실패")
    void fail_add_review_cuz_of_date() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (LocalDate.now().isAfter(matching.getDate().toLocalDate())) {
                throw new CustomException(NO_PERMISSION);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("매칭 기록없이 리뷰 작성 실패")
    void fail_add_review_cuz_of_no_matching_hisotry() {
        // given
        Optional<MatchingUser> userMatching = matchingUserRepository.findByUserId(user3.getId());

        // when

        // then
        assertThatThrownBy(() -> {
            if (userMatching.isEmpty()) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("리뷰 중복으로 작성 실패")
    void fail_add_review_cuz_of_duplicated() {
        // given
        PtReview review = ptReviewRepository.findByMemberIdAndTrainerId(user3.getId(), user4.getId());

        // when

        // then
        assertThatThrownBy(() -> {
            if (review.getId() != null) {
                throw new CustomException(DUPLICATED);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원이 아닌 트레이너면 리뷰 작성 실패")
    void fail_add_review_cuz_of_role() {
        // given
        // when

        // then
        assertThatThrownBy(() -> {
            if (!user2.getRole().equals(Role.USER)) {
                throw new CustomException(UNKNOWN_EXCEPTION);
            }
        }).isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("리뷰 조회 성공")
    void get_review() {
        // given

        // when
        PtReviewResponseDto response = reviewService.getPtReview(review.getId(), user3);

        // then
        assertThat(response.getStopReason()).isEqualTo("stop");
        assertThat(response.getContent()).isEqualTo("test review");
        assertThat(response.getReviewScore()).isEqualTo(4.5);
        assertThat(response.getUserName()).isEqualTo("test3");
        assertThat(response.getTrainerName()).isEqualTo("test4");
    }

    @Test
    @DisplayName("존재하지 않는 리뷰 조회 실패")
    void fail_review_cuz_of_no_review_history() {
        // given

        // when

        // then
        assertThatThrownBy(() -> reviewService.getPtReview(999L, user))
                .isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("해당 트레이너 리뷰 전체 조회 성공")
    void get_all_trainer_review() {
        // given
        Long trainerId = review.getTrainer().getId();

        // when
        List<PtReviewResponseDto> response = reviewService.getAllTrainerReview(trainerId, 0, 5);

        // then
        assertThat(response.get(0).getContent()).isEqualTo("test review");
        assertThat(response.get(0).getReviewScore()).isEqualTo(4.5);
        assertThat(response.get(0).getStopReason()).isEqualTo("stop");
        assertThat(response.get(0).getUserName()).isEqualTo(user3.getName());
        assertThat(response.get(0).getTrainerName()).isEqualTo(user4.getName());
    }

    @Test
    @DisplayName("내가 작성한 리뷰 전체 조회 성공")
    void get_all_review() {
        // given

        // when
        List<PtReviewResponseDto> response = reviewService.getAllReview(0, 5, user3);

        // then
        assertThat(response.get(0).getContent()).isEqualTo("test review");
        assertThat(response.get(0).getReviewScore()).isEqualTo(4.5);
        assertThat(response.get(0).getStopReason()).isEqualTo("stop");
        assertThat(response.get(0).getUserName()).isEqualTo(user3.getName());
        assertThat(response.get(0).getTrainerName()).isEqualTo(user4.getName());
    }

    @Test
    @DisplayName("내가 작성한 리뷰 전체 조회 실패 - user가 아닌 trainer임")
    void fail_get_all_review() {
        // given

        // when

        // then
        assertThatThrownBy(() -> reviewService.getAllReview(0, 5, user2))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void update_review() {
        // given
        PtReviewUpdateRequest dto = testSyUtils.updateReviewDto("update", "update", 4.0);

        // when
        PtReviewResponseDto saved = reviewService.updateReview(dto, review.getId(), user3);

        // then

        assertThat(saved.getContent()).isEqualTo("update");
        assertThat(saved.getStopReason()).isEqualTo("update");
        assertThat(saved.getReviewScore()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("다른 사람이 작성한 리뷰 수정 실패")
    void fail_other_review_update_cuz_of_role() {
        // given

        // when
        PtReviewUpdateRequest dto = testSyUtils.updateReviewDto("update", "update", 4.0);

        // then
        assertThatThrownBy(() -> reviewService.updateReview(dto, review.getId(), user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("트레이너가 리뷰 수정 실패")
    void fail_review_update_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user2.getRole().equals(Role.USER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete_pt_review() {
        // given

        // when
        PtReviewDeleteResponseDto response = reviewService.deletePtReview(review.getId(), user3);

        // then
        assertThat(response.getId()).isEqualTo(review.getId());
    }

    @Test
    @DisplayName("트레이가 리뷰 삭제 실패")
    void fail_trainer_delete_pt_review_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user2.getRole().equals(Role.USER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("다른 사람 리뷰 삭제 실패")
    void fail_other_delete_pt_review_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> reviewService.deletePtReview(review.getId(), user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("리뷰 검색 성공")
    void find_all() {
        // given
        String keyword = "review";

        // when
        Slice<PtReview> all = reviewService.findAll(keyword, 1L, Pageable.ofSize(10));

        // then
        assertThat(all.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("리뷰 조회 실패하기")
    void fail_find_all() {
        // given
        String keyword = "test";

        // when
        Slice<PtReview> response = reviewService.findAll(keyword, 0L, Pageable.ofSize(10));

        // then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("만들어진 리뷰 날짜 기준으로 필터링 성공")
    void find_all_cuz_of_date() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2024, 12, 8);

        // when
        List<PtReviewResponseDto> reviews = reviewService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(reviews).isNotNull();
        assertThat(reviews).isNotEmpty();
    }

    @Test
    @DisplayName("날짜 필터링 조회 실패로 리뷰 조회 실패")
    void fail_find_all_cuz_of_date() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2023, 12, 4);

        // when
        List<PtReviewResponseDto> reviews = reviewService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(reviews).isEmpty();
    }
}