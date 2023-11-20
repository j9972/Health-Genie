package com.example.healthgenie.service;

import com.example.healthgenie.domain.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.*;
import com.example.healthgenie.global.config.SecurityUtil;
import com.example.healthgenie.repository.MatchingRepository;
import com.example.healthgenie.repository.PtProcessRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PtProcessService {
    private final PtProcessRepository ptProcessRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;


    @Transactional
    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto, Long trainerId){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.NO_USER_INFO));

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.TRAINER_EMPTY));

        matchingRepository.findByMemberIdAndTrainerId(user.getId(), trainerId)
                .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

        return makePtRProcess(dto,trainer,user);
    }

    @Transactional
    public PtProcessResponseDto makePtRProcess(PtProcessRequestDto dto, User trainer, User user){

        PtProcess ptProcess = PtProcess.builder()
                .date(dto.getDate())
                .title(dto.getTitle())
                .content(dto.getContent())
                .photo(dto.getPhoto())
                .member(user)
                .trainer(trainer)
                .build();

        PtProcess process = ptProcessRepository.save(ptProcess);

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
             */
            User member = userRepository.findById(email.get().getId()).orElseThrow();
            boolean result = process.getMember().equals(member);

            if (result) {
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
        Page<PtProcess> process = ptProcessRepository.findAllByTrainerId(trainerId, PageRequest.of(page, size));
        return process.map(PtProcessResponseDto::of);
    }

    /*
        본인의 피드백들을 전부 모아보기
    */
    @Transactional(readOnly = true)
    public Page<PtProcessResponseDto> getAllMyProcess(Long userId, int page, int size){
        Page<PtProcess> process = ptProcessRepository.findAllByMemberId(userId, PageRequest.of(page, size));
        return process.map(PtProcessResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Long findById(Long processId) {
        Optional<PtProcess> process = ptProcessRepository.findById(processId);

        if (process.isPresent()) {
            return process.get().getTrainer().getId();
        }
        throw new PtProcessException(PtProcessErrorResult.NO_PROCESS_HISTORY);
    }

    @Transactional
    public void deletePtProcess(Long processId, Long trainerId) {

        userRepository.findById(trainerId)
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.TRAINER_EMPTY));

        PtProcess process = authorizationProcessWriter(processId,trainerId);

        ptProcessRepository.deleteById(process.getId());

    }

    public User isMemberCurrent(Long trainerId) {
        //log.info("SecurityUtil.getCurrentMemberId() : {}",SecurityUtil.getCurrentMemberId());
        return userRepository.findById(trainerId)
                .orElseThrow(() ->  new PtProcessException(PtProcessErrorResult.NO_USER_INFO));
    }

    public PtProcess authorizationProcessWriter(Long id, Long trainerId) {
        User member = isMemberCurrent(trainerId);

        PtProcess process = ptProcessRepository.findById(id).orElseThrow(() -> new PtProcessException(PtProcessErrorResult.RECORD_EMPTY));
        if (!process.getTrainer().equals(member)) {
            throw new PtProcessException(PtProcessErrorResult.WRONG_USER);
        }
        return process;
    }
}
