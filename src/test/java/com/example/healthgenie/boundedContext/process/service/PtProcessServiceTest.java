package com.example.healthgenie.boundedContext.process.service;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessDeleteResponseDto;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import com.example.healthgenie.boundedContext.process.process.service.PtProcessService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.service.UserService;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
class PtProcessServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    PtProcessService processService;

    @Autowired
    MatchingUserRepository matchingUserRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    UserService userService;

    User user;
    User user2;
    User user3;
    User user4;
    PtProcess process;
    Matching matching;

    @BeforeEach
    void before() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 11, 0, 0);
        LocalDate date2 = LocalDate.of(2023, 12, 5);

        user = testKrUtils.createUser("jh485200@gmail.com", "test1", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test@test.com", "test2", AuthProvider.EMPTY, Role.TRAINER);
        user3 = testKrUtils.createUser("test3@gmail.com", "test3", AuthProvider.EMPTY, Role.USER);
        user4 = testKrUtils.createUser("test4@test.com", "test4", AuthProvider.EMPTY, Role.TRAINER);

        userService.update(user, null, "test1", null, null, null, null, null);
        userService.update(user2, null, "test2", null, null, null, null, null);

        matching = testKrUtils.createMatching(user2, user.getId(), date, "체육관", "pt내용");
        process = testSyUtils.createProcess(date2, "test title2", "test content2", user, user2);
    }

    @Test
    @DisplayName("트레이너가 피드백 생성 성공")
    void make_process() {
        // given
        LocalDate date = LocalDate.of(2030, 2, 5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", "test1", "test2");

        // when
        PtProcessResponseDto response = processService.addPtProcess(dto, user2);

        // then
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getContent()).isEqualTo("test content");
        assertThat(response.getTitle()).isEqualTo("test title");
        assertThat(response.getUserName()).isEqualTo("test1");
        assertThat(response.getTrainerName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("피드백 작성 날짜가 매칭 날짜보다 이른 경우")
    void fail_make_process_cuz_of_date() {
        // given

        LocalDate date = LocalDate.of(2023, 2, 5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", "test1", "test2");

        // when

        // then
        assertThatThrownBy(() -> {
            if (matching.getDate().toLocalDate().isAfter(dto.getDate())) {
                throw new CustomException(NO_PERMISSION);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("매칭이 없어서 실패")
    void fail_make_process_cuz_of_empty_matching() {
        // given

        // when
        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(user4.getId());

        // then
        assertThatThrownBy(() -> {
            if (trainerMatchings.isEmpty()) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원이 피드백 생성 실패")
    void fail_make_process_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user.getRole().equals(Role.TRAINER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("피드백 상세 조회하기")
    void get_process() {
        // given
        LocalDate date = LocalDate.of(2023, 12, 5);

        // when
        PtProcessResponseDto response = processService.getPtProcess(process.getId(), user2);

        // then
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getContent()).isEqualTo("test content2");
        assertThat(response.getTitle()).isEqualTo("test title2");
        assertThat(response.getUserName()).isEqualTo("test1");
        assertThat(response.getTrainerName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("트레이너나 회원 외의 다른 사람이 피드백 상세 조회 실패하기")
    void fail_get_process_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user3.getNickname().equals(process.getMember().getNickname())
                    && !user3.getNickname().equals(process.getTrainer().getNickname())
            ) {
                throw new CustomException(DATA_NOT_FOUND);
            } else {
                processService.getPtProcess(process.getId(), user3);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("존재하지 않는 피드백 상세 조회 실패하기")
    void fail_get_process_cuz_of_no_process_history() {
        // given

        // when

        // then
        assertThatThrownBy(() -> processService.getPtProcess(999L, user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("트레이너가 작성한 본인의 모든 피드백 조회하기")
    void get_all_trainer_process() {
        // given

        // when
        List<PtProcessResponseDto> response = processService.getAllTrainerProcess(0, 5, user2);
        LocalDate date = LocalDate.of(2023, 12, 5);

        // then
        assertThat(response.get(0).getDate()).isEqualTo(date);
        assertThat(response.get(0).getContent()).isEqualTo("test content2");
        assertThat(response.get(0).getTitle()).isEqualTo("test title2");
        assertThat(response.get(0).getUserName()).isEqualTo("test1");
        assertThat(response.get(0).getTrainerName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("나의 모든 피드백 조회하기")
    void get_all_my_process() {
        // given

        // when
        List<PtProcessResponseDto> response = processService.getAllMyProcess(0, 5, user);
        LocalDate date = LocalDate.of(2023, 12, 5);

        // then
        assertThat(response.get(0).getDate()).isEqualTo(date);
        assertThat(response.get(0).getContent()).isEqualTo("test content2");
        assertThat(response.get(0).getTitle()).isEqualTo("test title2");
        assertThat(response.get(0).getUserName()).isEqualTo("test1");
        assertThat(response.get(0).getTrainerName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("트레이너가 피드백 삭제 성공하기")
    void delete_process() {
        // given

        // when
        PtProcessDeleteResponseDto response = processService.deletePtProcess(process.getId(), user2);

        // then
        assertThat(response.getId()).isEqualTo(process.getId());
    }

    @Test
    @DisplayName("회원이 피드백 삭제 실패하기")
    void fail_user_delete_pt_process_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user.getRole().equals(Role.TRAINER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("키워드로 조회하기")
    void find_all() {
        // given

        // when
        String keyword = "test";
        Slice<PtProcess> response = processService.findAll(keyword, 10L, Pageable.ofSize(10), user);

        // then
        assertThat(response.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("키워드로 조회 실페하기")
    void fail_find_all() {
        // given

        // when
        String keyword = "test";
        Slice<PtProcess> response = processService.findAll(keyword, 0L, Pageable.ofSize(10), user3);

        // then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("일지가 만들어진 날짜 기준으로 필터링으로 조회하기")
    void find_all_by_date() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2024, 12, 4);

        // when
        List<PtProcessResponseDto> process = processService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(process).isNotNull();
        assertThat(process).isNotEmpty();
        assertThat(process.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜 필터링 조회 실패로 일지 조회 실패")
    void fail_find_all_cuz_of_date() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2023, 12, 4);

        // when
        List<PtProcessResponseDto> process = processService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(process).isEmpty();
    }
}