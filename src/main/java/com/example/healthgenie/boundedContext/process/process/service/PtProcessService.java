package com.example.healthgenie.boundedContext.process.process.service;


import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.NOT_VALID;
import static com.example.healthgenie.base.exception.ErrorCode.NO_HISTORY;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessDeleteResponseDto;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import com.example.healthgenie.boundedContext.process.process.repository.PtProcessQueryRepository;
import com.example.healthgenie.boundedContext.process.process.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PtProcessService {
    private final PtProcessRepository ptProcessRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final PtProcessQueryRepository ptProcessQueryRepository;


    @Transactional
    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto, User currentUser) {

        User trainer = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        User user = userRepository.findByNickname(dto.getUserNickName())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        MatchingUser userMatching = matchingUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(trainer.getId());

        for (MatchingUser match : trainerMatchings) {
            // matching User안에 있는 값들중 matching id값이 같은 경우
            if (match.getMatching().getId().equals(userMatching.getMatching().getId())) {

                Matching matching = matchingRepository.findById(match.getMatching().getId())
                        .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

                // 작성 날짜가 매칭날짜보다 뒤에 있어야 한다
                if (dto.getDate().isAfter(matching.getDate().toLocalDate())) {
                    return makePtRProcess(dto, user, currentUser);
                }

                throw new CustomException(NOT_VALID, "날짜 선택이 잘못됨");

            }
        }

        throw new CustomException(DATA_NOT_FOUND, "매칭 데이터");
    }

    @Transactional
    public PtProcessResponseDto makePtRProcess(PtProcessRequestDto dto, User user, User currentUser) {

        checkRole(currentUser, Role.TRAINER);

        PtProcess process = dto.toEntity(user, currentUser);

        return PtProcessResponseDto.of(ptProcessRepository.save(process));

    }

    /*
        feedback은 해당 회원, 담당 트레이너만 볼 수 있다
        따라서 상세페이지 조회는 회원용, 트레이너용을 나눌 필요가 없다.

        해당 trainer, user만 확인 가능
    */
    @Transactional(readOnly = true)
    public PtProcessResponseDto getPtProcess(Long processId, User user) {
        PtProcess process = ptProcessRepository.findById(processId)
                .orElseThrow(() -> new CustomException(NO_HISTORY));

        // 권한 체크
        permission(user, process);

        return PtProcessResponseDto.of(process);
    }

    /*
        해당 트레이너가 작성한 모든 피드백들을 전부 모아보기
     */
    @Transactional(readOnly = true)
    public List<PtProcessResponseDto> getAllTrainerProcess(int page, int size, User currentUser) {

        checkRole(currentUser, Role.TRAINER);

        // 페이징 처리
        List<PtProcess> process = ptProcessQueryRepository.findAllByTrainerId(currentUser.getId(), page, size);
        return PtProcessResponseDto.of(process);

    }

    /*
        본인의 피드백들을 전부 모아보기
    */
    @Transactional(readOnly = true)
    public List<PtProcessResponseDto> getAllMyProcess(int page, int size, User currentUser) {

        checkRole(currentUser, Role.USER);

        // 페이징 처리
        List<PtProcess> process = ptProcessQueryRepository.findAllByMemberId(currentUser.getId(), page, size);
        return PtProcessResponseDto.of(process);
    }


    @Transactional
    public PtProcessDeleteResponseDto deletePtProcess(Long processId, User user) {

        // user가 trainer일때만 삭제 가능
        checkRole(user, Role.TRAINER);

        ptProcessRepository.deleteById(processId);

        return PtProcessDeleteResponseDto.builder()
                .id(processId)
                .build();
    }


    @Transactional(readOnly = true)
    public Slice<PtProcess> findAll(String keyword, Long lastId, Pageable pageable, User user) {
        return ptProcessQueryRepository.findAll(keyword, lastId, pageable, user);
    }

    @Transactional(readOnly = true)
    public List<PtProcessResponseDto> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return PtProcessResponseDto.of(ptProcessQueryRepository.findAllByDate(searchStartDate, searchEndDate));
    }

    private void checkRole(User currentUser, Role role) {
        if (!currentUser.getRole().equals(role)) {
            log.warn("role 오류. currentUser : {}", currentUser);
            throw new CustomException(NO_PERMISSION);
        }
    }

    private void permission(User user, PtProcess process) {
        if (!process.getMember().equals(user) && !process.getTrainer().equals(user)) {
            throw new CustomException(NO_PERMISSION, "권한");
        }
    }
}
