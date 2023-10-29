package com.example.healthgenie.service;

import com.example.healthgenie.domain.ptreview.dto.PtReviewDetailResponseDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewListResponseDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.domain.ptreview.entity.PtReivew;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.domain.ptreview.entity.UserPtReview;
import com.example.healthgenie.exception.PtReviewErrorResult;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.repository.UserPtReviewRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PtReviewService {

    private final UserPtReviewRepository userPtReviewRepository;
    private final UserRepository userRepository;

    public PtReviewResponseDto addPtReview(PtReviewRequestDto dto, Long userId){

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
                .title(dto.getTitle())
                .reviewContent(dto.getContents())
                .startDate(dto.getStartDate())
                .trainerName(dto.getTrainerName())
                .endDate(dto.getEndDate())
                .trainerName(dto.getTrainerName())
                .starScore(dto.getStarScore())
                .matching(TrainerPtApplication.builder().id(dto.getMatchingId()).build())
                .pic1(dto.getPic1())
                .pic2(dto.getPic2())
                .pic3(dto.getPic3())
                .build();
        PtReivew review = userPtReviewRepository.save(ptReview);

        return PtReviewResponseDto.builder()
                .reviewId(review.getId()).build();
    }

    public List<PtReviewListResponseDto> getReviewListByTrainer(Long trainerId){


        List<PtReivew> getReviewList = userPtReviewRepository.getAllByTrainer(User.builder().id(trainerId).build());
        List<PtReviewListResponseDto> resultList = new ArrayList<>();

        if(getReviewList.isEmpty()){
            return resultList;
        }

        return getReviewList.stream()
                .map(m -> new PtReviewListResponseDto(m.getId(), m.getTitle(), m.getStartDate(), m.getEndDate(),
                        m.getReviewContent(), m.getPic1(), m.getPic2(), m.getPic3(), m.getStarScore()))
                .collect(Collectors.toList());
    }

    public List<PtReviewListResponseDto> getReviewListByUser(Long userId){

        List<PtReivew> getReviewList = userPtReviewRepository.getAllByMember(User.builder().id(userId).build());
        List<PtReviewListResponseDto> resultList = new ArrayList<>();
        if(getReviewList.isEmpty()){
            return resultList;
        }
        return getReviewList.stream()
                .map(m -> new PtReviewListResponseDto(m.getId(), m.getTitle(), m.getStartDate(), m.getEndDate(),
                        m.getReviewContent(), m.getPic1(), m.getPic2(), m.getPic3(), m.getStarScore()))
                .collect(Collectors.toList());
    }

    public PtReviewDetailResponseDto getReviewDetail(Long reviewId){

        PtReivew result = userPtReviewRepository.findsById(reviewId);
        return toDetailResponse(result);
    }

    public PtReviewDetailResponseDto toDetailResponse(PtReivew review){
        return PtReviewDetailResponseDto.builder()
                .id(review.getId())
                .startDate(review.getStartDate())
                .endDate(review.getEndDate())
                .title(review.getTitle())
                .reviewContent(review.getReviewContent())
                .starScore(review.getStarScore())
                .trainerId(review.getTrainer().getId())
                .trainerName(review.getTrainerName())
                .pic1(review.getPic1())
                .pic2(review.getPic2())
                .pic3(review.getPic3())
                .build();
    }
}
