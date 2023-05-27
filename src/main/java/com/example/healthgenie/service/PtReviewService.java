package com.example.healthgenie.service;

import com.example.healthgenie.dto.PtReviewRequestDto;
import com.example.healthgenie.dto.PtReviewResponseDto;
import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.TrainerPtApplication;
import com.example.healthgenie.entity.User;
import com.example.healthgenie.entity.UserPtReview;
import com.example.healthgenie.exception.PtReviewErrorResult;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.repository.UserPtReviewRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PtReviewService {

    private final UserPtReviewRepository userPtReviewRepository;
    private final UserRepository userRepository;

    public PtReviewResponseDto addPtReview(PtReviewRequestDto dto, Long userId){

        UserPtReview result = userPtReviewRepository.findByMatchingId(dto.getMatchingId());
        if(result!= null){
            throw new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW);
        }
        User trainer =userRepository.findByRoleAndId(dto.getRole(),dto.getTrainerId());
        if(trainer == null){
            throw new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY);
        }
        //dto가 필요하네
        //무슨 정보를넣을지
        UserPtReview ptReview = UserPtReview.builder()
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
        UserPtReview review = userPtReviewRepository.save(ptReview);

        return PtReviewResponseDto.builder()
                .reviewId(review.getId()).build();
    }
}
