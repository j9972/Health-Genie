package com.example.healthgenie.boundedContext.ptrecord.service;


import com.example.healthgenie.base.exception.MatchingErrorResult;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.exception.PtProcessErrorResult;
import com.example.healthgenie.base.exception.PtProcessException;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.querydsl.core.types.OrderSpecifier;
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

import java.util.Optional;

import static com.example.healthgenie.boundedContext.ptrecord.entity.QPtProcess.ptProcess;

@Service
@Slf4j
@RequiredArgsConstructor
public class PtProcessService {
    private final PtProcessRepository ptProcessRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;


    @Transactional
    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto){

        User user = userRepository.findByEmail(dto.getUserMail())
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.NO_USER_INFO));

        matchingRepository.findByMemberEmailAndTrainerEmail(user.getEmail(), dto.getTrainerMail())
                .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

        return makePtRProcess(dto,user);
    }

    @Transactional
    public PtProcessResponseDto makePtRProcess(PtProcessRequestDto dto, User user) {

        User currentUser = SecurityUtils.getCurrentUser();

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

    @Transactional(readOnly = true)
    public PtProcessResponseDto getPtProcess(Long processId) {
        PtProcess process = ptProcessRepository.findById(processId).orElseThrow(
                () -> new PtProcessException(PtProcessErrorResult.NO_PROCESS_HISTORY));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        } else {

            Optional<User> email = userRepository.findByEmail(authentication.getName());
            /*
                feedback은 해당 회원, 담당 트레이너만 볼 수 있다
                따라서 상세페이지 조회는 회원용, 트레이너용을 나눌 필요가 없다.

                해당 trainer, user만 확인 가능
             */
            User member = userRepository.findById(email.get().getId()).orElseThrow();
            boolean user_result = process.getMember().equals(member);
            boolean trainer_result = process.getTrainer().equals(member);

            if (user_result)  {
                return PtProcessResponseDto.of(process);
            } else if (trainer_result) {
                return PtProcessResponseDto.of(process);
            } else {
                throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
            }
        }
    }


    /*
        해당 트레이너가 작성한 모든 피드백들을 전부 모아보기
     */
    @Transactional(readOnly = true)
    public Page<PtProcessResponseDto> getAllTrainerProcess(Long trainerId, int page, int size){

        // 작성 시간 역순으로 정렬 (가장 최근 작성 순)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtProcess> process = ptProcessRepository.findAllByTrainerId(trainerId, pageable);
        return process.map(PtProcessResponseDto::of);

    }

    /*
        본인의 피드백들을 전부 모아보기
    */
    @Transactional(readOnly = true)
    public Page<PtProcessResponseDto> getAllMyProcess(Long userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<PtProcess> process = ptProcessRepository.findAllByMemberId(userId, pageable);
        return process.map(PtProcessResponseDto::of);
    }


    @Transactional
    public void deletePtProcess(Long processId) {

        PtProcess process = authorizationProcessWriter(processId);

        ptProcessRepository.deleteById(process.getId());

    }

    public User isMemberCurrent() {
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->  new PtProcessException(PtProcessErrorResult.NO_USER_INFO));
    }

    // process는 트레이너만 수정 삭제 가능
    public PtProcess authorizationProcessWriter(Long id) {
        User member = isMemberCurrent();

        PtProcess process = ptProcessRepository.findById(id).orElseThrow(() -> new PtProcessException(PtProcessErrorResult.RECORD_EMPTY));
        if (!process.getTrainer().equals(member)) {
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        }
        return process;
    }

}
