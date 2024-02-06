package com.example.healthgenie.boundedContext.ptrecord.service;


import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.MatchingErrorResult;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.exception.PtProcessErrorResult;
import com.example.healthgenie.base.exception.PtProcessException;
import com.example.healthgenie.base.exception.UserErrorResult;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessDeleteResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessQueryRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        User user = userRepository.findByNickname(dto.getUserNickName())
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        MatchingUser userMatching = matchingUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(trainer.getId());

        for (MatchingUser match : trainerMatchings) {
            // matching User안에 있는 값들중 matching id값이 같은 경우
            if (match.getMatching().getId().equals(userMatching.getMatching().getId())) {

                Matching matching = matchingRepository.findById(match.getMatching().getId())
                        .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

                // trainer와 user사이의 매칭이 있을때 일지 작성 가능
                log.info("해당하는 매칭이 있음 matching : {}", matching);

                // 작성 날짜가 매칭날짜보다 뒤에 있어야 한다
                if (dto.getDate().isAfter(matching.getDate())) {
                    return makePtRProcess(dto, user, currentUser);
                }

                log.warn("일지 작성 날짜가 매칭날짜보다 뒤에 있어야 하는데 그렇지 못함");
                throw new PtProcessException(PtProcessErrorResult.WRONG_DATE);

            }
        }

        log.warn("해당하는 매칭이 없음");
        throw new MatchingException(MatchingErrorResult.MATCHING_EMPTY);
    }

    @Transactional
    public PtProcessResponseDto makePtRProcess(PtProcessRequestDto dto, User user, User currentUser) {

        checkRole(currentUser, Role.TRAINER);

        PtProcess process = PtProcess.builder()
                .date(dto.getDate())
                .content(dto.getContent())
                .title(dto.getTitle())
                .member(user)
                .trainer(currentUser)
                .build();

        //PtProcess process = dto.toEntity(user, currentUser);

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
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.NO_PROCESS_HISTORY));

        // 권한 체크
        checkRole(user, Role.TRAINER);

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

        PtProcess process = authorizationProcessWriter(processId, user);

        ptProcessRepository.deleteById(process.getId());

        return PtProcessDeleteResponseDto.builder()
                .id(process.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PtProcessResponseDto> findAll(String keyword) {
        return PtProcessResponseDto.of(ptProcessQueryRepository.findAll(keyword));
    }

    @Transactional(readOnly = true)
    public List<PtProcessResponseDto> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return PtProcessResponseDto.of(ptProcessQueryRepository.findAllByDate(searchStartDate, searchEndDate));
    }

    // process는 트레이너만 수정 삭제 가능
    private PtProcess authorizationProcessWriter(Long id, User member) {

        PtProcess process = ptProcessRepository.findById(id)
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.RECORD_EMPTY));
        authCheck(member, process);

        return process;
    }

    private void authCheck(User member, PtProcess process) {
        if (!process.getTrainer().getId().equals(member.getId())) {
            log.warn("process 소유자 user : {}", member);
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        }
    }

    private void checkRole(User currentUser, Role role) {
        if (!currentUser.getRole().equals(role)) {
            log.warn("role 오류. currentUser : {}", currentUser);
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }
    }
}
