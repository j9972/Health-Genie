package com.example.healthgenie.boundedContext.ptrecord.service;


import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessQueryRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto, User currentUser){

        Optional<User> trainer = userRepository.findByNickname(currentUser.getNickname());
        Optional<User> user = userRepository.findByNickname(dto.getUserNickName());

        if (user.isEmpty() || trainer.isEmpty()) {
            log.warn("user or trainer is not found user : {} , trainer : {}", user, trainer);
            throw new UserException(UserErrorResult.USER_NOT_FOUND);
        }

        Optional<MatchingUser> userMatching = matchingUserRepository.findByUserId(user.get().getId());
        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(trainer.get().getId());

        if(userMatching.isEmpty()) {
            log.warn("user의 matching 기록이 없습니다");
            throw new MatchingException(MatchingErrorResult.MATCHING_EMPTY);
        }

        for(MatchingUser match : trainerMatchings) {
            // matching User안에 있는 값들중 matching id값이 같은 경우
            if(match.getMatching().getId().equals(userMatching.get().getMatching().getId())) {

                Optional<Matching> matching = matchingRepository.findById(match.getMatching().getId());

                // trainer와 user사이의 매칭이 있을때 일지 작성 가능
                log.info("해당하는 매칭이 있음 matching : {}", matching);

                // 작성 날짜가 매칭날짜보다 뒤에 있어야 한다
                if(dto.getDate().isAfter(matching.get().getDate())) {
                    return makePtRProcess(dto,user.get(),currentUser);
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

            Optional<User> email = userRepository.findByEmail(authentication.getName());
            User member = userRepository.findById(email.get().getId()).orElseThrow();

            boolean user_result = process.getMember().equals(member);
            boolean trainer_result = process.getTrainer().equals(member);

            if (user_result || trainer_result)  {
                return PtProcessResponseDto.of(process);
            }
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);

        }
    }


    /*
        해당 트레이너가 작성한 모든 피드백들을 전부 모아보기
     */
    @Transactional(readOnly = true)
    public Page<PtProcessResponseDto> getAllTrainerProcess(int page, int size, User currentUser){

        if (!currentUser.getRole().equals(Role.TRAINER)) {
            log.warn("trainer 본인이 작성한 process 조회 -> 작성자가 trainer가 아님. currentUser : {}", currentUser);
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }

        // 작성 시간 역순으로 정렬 (가장 최근 작성 순)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtProcess> process = ptProcessRepository.findAllByTrainerId(currentUser.getId(), pageable);
        return process.map(PtProcessResponseDto::of);

    }

    /*
        본인의 피드백들을 전부 모아보기
    */
    @Transactional(readOnly = true)
    public Page<PtProcessResponseDto> getAllMyProcess(int page, int size, User currentUser){

        if (!currentUser.getRole().equals(Role.USER)) {
            log.warn("process 조회 -> 조회자가 user 본인이 아님.  currentUser : {}", currentUser);
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtProcess> process = ptProcessRepository.findAllByMemberId(currentUser.getId(), pageable);
        return process.map(PtProcessResponseDto::of);
    }


    @Transactional
    public String deletePtProcess(Long processId, User user) {

        PtProcess process = authorizationProcessWriter(processId, user);

        ptProcessRepository.deleteById(process.getId());

        return "피드백이 삭제 되었습니다.";

    }

    // process는 트레이너만 수정 삭제 가능
    public PtProcess authorizationProcessWriter(Long id, User member) {

        PtProcess process = ptProcessRepository.findById(id).orElseThrow(() -> new PtProcessException(PtProcessErrorResult.RECORD_EMPTY));
        if (!process.getTrainer().getId().equals(member.getId())) {
            log.warn("process 소유자 user : {}", member);
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        }
        return process;
    }

    public List<PtProcessResponseDto> findAll(String keyword) {

        return PtProcessResponseDto.of(ptProcessQueryRepository.findAll(keyword));
    }

    public List<PtProcessResponseDto> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return PtProcessResponseDto.of(ptProcessQueryRepository.findAllByDate(searchStartDate, searchEndDate));
    }
}
