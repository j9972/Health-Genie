package com.example.healthgenie.boundedContext.ptrecord.service;


import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.MatchingErrorResult;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.exception.PtProcessErrorResult;
import com.example.healthgenie.base.exception.PtProcessException;
import com.example.healthgenie.base.exception.PtReviewErrorResult;
import com.example.healthgenie.base.exception.PtReviewException;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        User trainer = userRepository.findByNickname(currentUser.getNickname()).orElseThrow();
        User user = userRepository.findByNickname(dto.getUserNickName()).orElseThrow();

        MatchingUser userMatching = matchingUserRepository.findByUserId(user.getId()).orElseThrow();
        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(trainer.getId());

        for (MatchingUser match : trainerMatchings) {
            // matching User안에 있는 값들중 matching id값이 같은 경우
            if (match.getMatching().getId().equals(userMatching.getMatching().getId())) {

                Matching matching = matchingRepository.findById(match.getMatching().getId()).orElseThrow();

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

        if (!currentUser.getRole().equals(Role.TRAINER)) {
            log.warn("process 작성 -> 작성자가 trainer가 아님");
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER_ROLE);
        }

        PtProcess process = ptProcessRepository.save(
                PtProcess.builder()
                        .date(dto.getDate())
                        .content(dto.getContent())
                        .title(dto.getTitle())
                        .member(user)
                        .trainer(currentUser)
                        .build()
        );

        return PtProcessResponseDto.of(process);
    }

    /*
        feedback은 해당 회원, 담당 트레이너만 볼 수 있다
        따라서 상세페이지 조회는 회원용, 트레이너용을 나눌 필요가 없다.

        해당 trainer, user만 확인 가능
    */
    @Transactional(readOnly = true)
    public PtProcessResponseDto getPtProcess(Long processId) {
        PtProcess process = ptProcessRepository.findById(processId).orElseThrow(
                () -> new PtProcessException(PtProcessErrorResult.NO_PROCESS_HISTORY));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            log.warn("process 조회 -> 작성자가 트레이너가 아님");
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);

        } else {

            User email = userRepository.findByEmail(authentication.getName()).orElseThrow();
            User member = userRepository.findById(email.getId()).orElseThrow();

            boolean user_result = process.getMember().equals(member);
            boolean trainer_result = process.getTrainer().equals(member);

            if (user_result || trainer_result) {
                return PtProcessResponseDto.of(process);
            }
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);

        }
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
    public String deletePtProcess(Long processId, User user) {

        PtProcess process = authorizationProcessWriter(processId, user);

        ptProcessRepository.deleteById(process.getId());

        return "피드백이 삭제 되었습니다.";
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
        if (!process.getTrainer().getId().equals(member.getId())) {
            log.warn("process 소유자 user : {}", member);
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        }
        return process;
    }

    private void checkRole(User currentUser, Role role) {
        if (!currentUser.getRole().equals(role)) {
            log.warn("role 오류. currentUser : {}", currentUser);
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }
    }
}
