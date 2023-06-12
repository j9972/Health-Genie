package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.dto.*;
import com.example.healthgenie.domain.trainer.entity.TrainerProfile;
import com.example.healthgenie.exception.TrainerProfileErrorResult;
import com.example.healthgenie.exception.TrainerProfileException;
import com.example.healthgenie.repository.TrainerProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TrainerProfileServieTest {

    @InjectMocks
    TrainerProfileServie target;

    @Mock
    TrainerProfileRepository repository;

    @Test
    public void 약력작성실패_없음(){


//        repository.save()
    }
    @Test
    public void 약력작성성공(){

        //given
        TrainerProfile givenProfile = TrainerProfile.builder().avgSarScore(5L).name("jingwon").build();
        doReturn(TrainerProfile.builder().avgSarScore(5L).name("jingwon").id(1L).build()).when(repository).save(any(TrainerProfile.class));

        //when
        final TrainerProfileResponseDto result = target.profileAdd(TrainerProfileRequestDto.builder().build(),1L);


        //then
        assertThat(result.getProfileId()).isNotNull();
    }


    @Test
    public void 약력수정실패(){

    }

    @Test
    public void 약력수정성공(){


        //given
        TrainerProfileModifyRequestDto dto = TrainerProfileModifyRequestDto.builder().profileId(1L).build();
        TrainerProfile givenProfile = TrainerProfile.builder().avgSarScore(5L).name("jingwon").id(1L).build();
        Optional<TrainerProfile> findProfile = Optional.of(givenProfile);

        TrainerProfile saveProfile = TrainerProfile.builder()
                .id(givenProfile.getId())
                .name(givenProfile.getName())
                .avgSarScore(givenProfile.getAvgSarScore())
                .description(dto.getDescription())
                .prize(dto.getPrize())
                .pics(dto.getPics())
                .certification(dto.getCertification())
                .build();

        doReturn(findProfile).when(repository).findById(1L);
        doReturn(saveProfile).when(repository).save(any(TrainerProfile.class));

        //when
        final TrainerProfileModifiyResponseDto result = target.profileModify(dto,1L);


        //then
        assertThat(result.getProfileId()).isNotNull();

    }

    @Test
    public void 약력조회실패(){

        //given
        Optional<TrainerProfile> profile = Optional.empty();
        doReturn(profile).when(repository).findById(any(Long.class));
        //when
        final TrainerProfileException result = assertThrows(TrainerProfileException.class, () -> target.profileGet(1L));
        //then
        assertThat(result).isNotNull();
        assertThat(result.getTrainerProfileErrorResult()).isEqualTo(TrainerProfileErrorResult.PROFILE_EMPTY);
    }

    @Test
    public void 약력조회성공(){
        //given

        Optional<TrainerProfile> profile = Optional.ofNullable(TrainerProfile.builder().name("test").id(1L).build());
        doReturn(profile).when(repository).findById(any(Long.class));
        //when

        TrainerProfileGetResponseDto result = target.profileGet(profile.get().getId());
        //then

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("test");

    }
}



