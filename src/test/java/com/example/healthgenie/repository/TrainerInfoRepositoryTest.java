package com.example.healthgenie.repository;

import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TrainerInfoRepositoryTest {


    @Autowired
    private TrainerInfoRepository repository;

    //약력 추가
    @Test
    public void ProfileAdd(){

        //given
        TrainerInfo trainerInfo = TrainerInfo.builder()
                .avgSarScore(0L)
                .certification("약력/증명서")
                .description("열심히 할게요")
                .name("trainer")
                .matchingTimes(0L)
                .prize("tntkd")
                .build();

        //when
        TrainerInfo result = repository.save(trainerInfo);

        //then

        assertThat(result.getAvgSarScore()).isEqualTo(0L);
        assertThat(result.getCertification()).isEqualTo("약력/증명서");
        assertThat(result.getName()).isEqualTo("trainer");
    }

    //기존 약력 수정
    @Test
    public void profileModified(){
        //given
        TrainerInfo trainerInfo = TrainerInfo.builder()
                .avgSarScore(0L)
                .certification("약력/증명서")
                .description("열심히 할게요")
                .name("trainer")
                .matchingTimes(0L)
                .prize("tntkd")
                .build();
        TrainerInfo saveProfile = repository.save(trainerInfo);
        assertThat(saveProfile.getDescription()).isEqualTo("열심히 할게요");

        //when
        TrainerInfo modifiedProfile = repository.findById(saveProfile.getId()).get();
        modifiedProfile = TrainerInfo.builder()
                .id(saveProfile.getId())
                .avgSarScore(0L)
                .certification("약력/수정증명서")
                .description("열심히 수정할게요")
                .name("trainer")
                .matchingTimes(0L)
                .prize("tntks")
                .build();
        //saveProfile과 modifedProfile은 같은 객체 이다. 얕은복사) -> findById로 saveProfile의 데이터를 가져온순간부터 modifiedProfile = savProfile은 공유된다. 1개의값을 변경하면 같이 변경됨
        //약력의 경우 트레이너와 onetoone임으로 객체 공유되도 크게 상관없지만 다른경우 dto를 사용하자 set을 쓰지않고

        TrainerInfo result = repository.save(modifiedProfile);

        //then
        assertThat(result.getDescription()).isEqualTo("열심히 수정할게요");
        assertThat(result.getCertification()).isEqualTo("약력/수정증명서");
        assertThat(result.getName()).isEqualTo("trainer");
        assertThat(result.getId()).isEqualTo(saveProfile.getId());
    }

    @Test
    public void profileGet(){

        //given
        final TrainerInfo profile = TrainerInfo.builder()
                .build();
        final TrainerInfo saveProfile = repository.save(profile);
        //when
        final Optional<TrainerInfo> findProfile = repository.findById(saveProfile.getId());

        //then
        assertThat(findProfile.get().getId()).isEqualTo(saveProfile.getId());
    }

}
