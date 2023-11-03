package com.example.healthgenie.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@DataJpaTest
@Transactional
public class UserPtReviewRepositoryTest {

    @Autowired
    private PtReviewRepository ptReviewRepository;
    @Autowired
    private UserRepository userRepository;


    //후기 작성하는지 확인
    /*
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

    @Test
    public void getAllByTrainer(){
        //given
        final User trianer = User.builder().name("trainer").role(Role.GUEST).build();
        final User user = User.builder().name("user").role(Role.USER).build();
        final User relsutUser = userRepository.save(trianer);
        final User rUser = userRepository.save(user);

        final UserPtReview review1 = UserPtReview.builder().trainer(trianer).member(user).build();
        final UserPtReview review2 = UserPtReview.builder().trainer(trianer).member(user).build();

        final UserPtReview result = userPtReviewRepository.save(review1);
        final UserPtReview result2 = userPtReviewRepository.save(review2);

        //when
        final List<UserPtReview> reviewList = userPtReviewRepository.getAllByTrainer(relsutUser);
        //then

        assertThat(reviewList.size()).isEqualTo(2);
    }


    @Test
    public void getAllByUser(){

        //given

        final User trainer = User.builder().name("trainer").role(Role.GUEST).build();
        final User user = User.builder().name("user").role(Role.USER).build();
        final User relsutUser = userRepository.save(user);
        final User resultTrainer = userRepository.save(trainer);


        final UserPtReview review1 = UserPtReview.builder().trainer(trainer).member(user).build();
        final UserPtReview review2 = UserPtReview.builder().trainer(trainer).member(user).build();

        final UserPtReview result = userPtReviewRepository.save(review1);
        final UserPtReview result2 = userPtReviewRepository.save(review2);

        //when
        final List<UserPtReview> reviewList = userPtReviewRepository.getAllByMember(relsutUser);

        //then

        assertThat(reviewList.size()).isEqualTo(2);

    }


     */
}
