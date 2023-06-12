package com.example.healthgenie.service;

import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.domain.ptreview.entity.UserPtReview;
import com.example.healthgenie.domain.trainer.entity.TrainerPtApplication;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.PtReviewErrorResult;
import com.example.healthgenie.exception.PtReviewException;
import com.example.healthgenie.repository.UserPtReviewRepository;
import com.example.healthgenie.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserPtReviewServiceTest {


//    private final String userId;

    @InjectMocks
    private PtReviewService target;
    @Mock
    private UserPtReviewRepository userPtReviewRepository;
    @Mock
    private UserRepository userRepository;

    private final Role role = Role.GUEST;

    private final Long trainerId =1L;
    private final Long matchingId =1L;
    private final Long Id =1L;


    private PtReviewRequestDto dto = PtReviewRequestDto.builder().build();
    @Test
    public void 후기작성실패_이미존재할경우(){


        //given
        dto.setMatchingId(matchingId);
        dto.setTrainerId(trainerId);
        doReturn(UserPtReview.builder().build()).when(userPtReviewRepository).findByMatchingId(matchingId);

        //when
        final PtReviewException result = assertThrows(PtReviewException.class, () -> target.addPtReview(dto,Id));
        //


        //then

        assertThat(result.getPtReviewErrorResult()).isEqualTo(PtReviewErrorResult.DUPLICATED_REVIEW);
        //
    }
    @Test
    public void 후기작성실패_트레이너가존재하지않을경우(){
        //save해야됨 근데 하기전에 트레이너가 있는지 검사
        //given
        dto.setTrainerId(trainerId);
        dto.setRole(Role.GUEST);
        doReturn(null).when(userRepository).findByRoleAndId(dto.getRole(),dto.getTrainerId());

        //when
        final PtReviewException result = assertThrows(PtReviewException.class, () -> target.addPtReview(dto,Id));

        //then
        assertThat(result.getPtReviewErrorResult()).isEqualTo(PtReviewErrorResult.TRAINER_EMPTY);
    }

    @Test
    public void 후기작성성공(){
        //성공하는 걸로 할려면 given에 findsByid 와 findByRoleAndId에 올바른 값 리턴ㄴ하게 한다.

        //given
        dto.setTrainerName("jingwon");
        dto.setTrainerId(1L);
        dto.setRole(Role.GUEST);
        dto.setMatchingId(1L);
        User trainer = User.builder().id(1L).email("email").role(Role.GUEST).build();

        doReturn(null).when(userPtReviewRepository).findByMatchingId(dto.getMatchingId());
        doReturn(trainer).when(userRepository).findByRoleAndId(dto.getRole(),dto.getTrainerId());
        doReturn(UserPtReview()).when(userPtReviewRepository).save(any(UserPtReview.class));


        //when
        final PtReviewResponseDto result = target.addPtReview(dto,Id);


        //then
        assertThat(result.getReviewId()).isNotNull();
//        assertThat(result.getTrainerName()).isEqualTo("jingwon");
//        assertThat(result.getMatching().getId()).isEqualTo(dto.getMatchingId());

    }
    public UserPtReview UserPtReview(){
        return UserPtReview.builder().id(-1L)
                .matching(TrainerPtApplication.builder().id(dto.getMatchingId()).build())
                .trainerName(dto.getTrainerName())
                .trainer(User.builder().id(Id).build())
                .build();
    }

}
