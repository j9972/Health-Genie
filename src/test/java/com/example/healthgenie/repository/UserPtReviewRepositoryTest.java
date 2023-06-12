package com.example.healthgenie.repository;


import com.example.healthgenie.domain.matching.entity.MatchingState;
import com.example.healthgenie.domain.ptreview.entity.UserPtReview;
import com.example.healthgenie.domain.trainer.entity.TrainerPtApplication;
import com.example.healthgenie.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
public class UserPtReviewRepositoryTest {

    @Autowired
    private UserPtReviewRepository userPtReviewRepository;


    //후기 작성하는지 확인
    @Test
    public void reviewAdd(){
        //given
        User member = User.builder()
                .email("email.com")
                .build();
        User trainer = User.builder()
                .email("trainer.com")
                .build();
        TrainerPtApplication match = TrainerPtApplication.builder()
                .member(member)
                .trainer(trainer)
                .ptStartDate("2023-09-01")
                .matchingState(MatchingState.YES).build();
        final UserPtReview review = UserPtReview.builder()
                .endDate("2023-09-01")
                .reviewContent("test review")
                .startDate("2023-03-01")
                .title("엉덩이 맛나게 먹음")
                .trainer(trainer)
                .member(member)
                .matching(match)
                .trainerName("jingwon")
                .starScore(10)
                .build();

        //when
        final UserPtReview resultReview = userPtReviewRepository.save(review);

        //then
        assertThat(resultReview.getId()).isNotNull();
        assertThat(resultReview.getMember().getEmail()).isEqualTo("email.com");
        assertThat(resultReview.getTrainer().getEmail()).isEqualTo("trainer.com");
        assertThat(resultReview.getReviewContent()).isEqualTo("test review");
        assertThat(resultReview.getEndDate()).isEqualTo("2023-09-01");
        assertThat(resultReview.getMatching().getId()).isEqualTo(match.getId());
    }


    //존재하는 지 확인
    @Test
    public void find(){

        //given
        final UserPtReview review = UserPtReview.builder()
                .build();
        final UserPtReview resultReview = userPtReviewRepository.save(review);

        //when
        final UserPtReview findReview = userPtReviewRepository.findsById(resultReview.getId());

        //then
        assertThat(findReview.getId()).isEqualTo(resultReview.getId());
    }


}
