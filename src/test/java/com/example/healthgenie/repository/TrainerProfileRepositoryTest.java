package com.example.healthgenie.repository;

import com.example.healthgenie.entity.TrainerProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Repository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TrainerProfileRepositoryTest {


    @Autowired
    private TrainerProfileRepository repository;

    //약력 추가
    @Test
    public void ProfileAdd(){

        //given
        TrainerProfile trainerProfile = TrainerProfile.builder()
                .avgSarScore(0L)
                .certification("약력/증명서")
                .description("열심히 할게요")
                .name("trainer")
                .matchingTimes(0L)
                .prize("tntkd")
                .build();

        //when
        TrainerProfile result = repository.save(trainerProfile);

        //then

        assertThat(result.getAvgSarScore()).isEqualTo(0L);
        assertThat(result.getCertification()).isEqualTo("약력/증명서");
        assertThat(result.getName()).isEqualTo("trainer");
    }

    //기존 약력 수정
    @Test
    public void profileModified(){
        //given
        TrainerProfile trainerProfile = TrainerProfile.builder()
                .avgSarScore(0L)
                .certification("약력/증명서")
                .description("열심히 할게요")
                .name("trainer")
                .matchingTimes(0L)
                .prize("tntkd")
                .build();
        TrainerProfile saveProfile = repository.save(trainerProfile);
        assertThat(saveProfile.getDescription()).isEqualTo("열심히 할게요");

        //when
        TrainerProfile modifiedProfile = repository.findById(saveProfile.getId()).get();
        modifiedProfile = TrainerProfile.builder()
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

        TrainerProfile result = repository.save(modifiedProfile);

        //then
        assertThat(result.getDescription()).isEqualTo("열심히 수정할게요");
        assertThat(result.getCertification()).isEqualTo("약력/수정증명서");
        assertThat(result.getName()).isEqualTo("trainer");
        assertThat(result.getId()).isEqualTo(saveProfile.getId());
    }

}
