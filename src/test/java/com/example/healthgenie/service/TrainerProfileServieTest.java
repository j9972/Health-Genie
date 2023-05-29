package com.example.healthgenie.service;

import com.example.healthgenie.dto.TrainerProfileModifiyResponseDto;
import com.example.healthgenie.dto.TrainerProfileModifyRequestDto;
import com.example.healthgenie.dto.TrainerProfileRequestDto;
import com.example.healthgenie.dto.TrainerProfileResponseDto;
import com.example.healthgenie.entity.TrainerProfile;
import com.example.healthgenie.repository.TrainerProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
}



