package com.example.healthgenie.service;

import com.example.healthgenie.domain.ptreview.dto.PtReviewListResponseDto;
import com.example.healthgenie.domain.ptreview.entity.PtReivew;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.repository.PtReviewRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PtReviewService {

    private final PtReviewRepository ptReviewRepository;
    private final UserRepository userRepository;

    /*
    public PtReviewListResponseDto addPtReview(PtReviewListResponseDto dto, Long userId){

        PtReivew result = userPtReviewRepository.findByMatchingId(dto.getMatchingId());
        if(result!= null){
            throw new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW);
        }

        User trainer =userRepository.findByRoleAndId(dto.getRole(),dto.getTrainerId());
        if(trainer == null){
            throw new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY);
        }

        //dto가 필요하네
        //무슨 정보를넣을지
        PtReivew ptReview = PtReivew.builder()
                .member(User.builder().id(userId).build())
                .trainer(User.builder().id(dto.getTrainerId()).build())
                .build();
        PtReivew review = userPtReviewRepository.save(ptReview);

        return PtReviewListResponseDto.builder()
                .reviewId(review.getId()).build();
    }

     */


    public List<PtReviewListResponseDto> getReviewListByTrainer(Long trainerId){
        // User.builder().id(trainerId).build()

        List<PtReivew> getReviewList = ptReviewRepository.getAllByTrainerId(trainerId);
        List<PtReviewListResponseDto> resultList = new ArrayList<>();

        if(getReviewList.isEmpty()){
            return resultList;
        }

        return getReviewList.stream()
                .map(m -> new PtReviewListResponseDto(m.getId(), m.getContent(), m.getStopReason(), m.getReviewScore() , m.getTrainer().getId(), m.getMember().getId()))
                .collect(Collectors.toList());
    }


    public List<PtReviewListResponseDto> getReviewListByUser(Long userId){
        //User.builder().id(userId).build()

        List<PtReivew> getReviewList = ptReviewRepository.getAllByMemberId(userId);
        List<PtReviewListResponseDto> resultList = new ArrayList<>();
        if(getReviewList.isEmpty()){
            return resultList;
        }
        return getReviewList.stream()
                .map(m -> new PtReviewListResponseDto(m.getId(), m.getContent(), m.getStopReason(), m.getReviewScore() , m.getTrainer().getId(), m.getMember().getId()))
                .collect(Collectors.toList());
    }

}
