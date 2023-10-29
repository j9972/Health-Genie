package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.dto.*;
import com.example.healthgenie.repository.TrainerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerProfileServie {
    private final TrainerProfileRepository trainerProfileRepository;


    //trainer와 onetoone관계 - 트레이너Id넣고 save하면 같은거 찾아서 업데이트함
    //약력작성 전에 trainer정보를 미리 보내놓기 때문에 trainer정보가 틀릴일이 없다.
    //트레이너 정보를 보내는 과정에서 트레이너 id가 틀릴 경우가 검증되어있음 그냥 save하면 된다.

    //약력 작성
//    public TrainerProfileResponseDto profileAdd(TrainerProfileRequestDto profileRequestDto, Long Id,String filePath){
//
//        TrainerProfile saveProfile = TrainerProfile.builder()
//                .avgSarScore(profileRequestDto.getAvgSarScore())
//                .name(profileRequestDto.getName())
//                .description(profileRequestDto.getDescription())
//                .certification(profileRequestDto.getCertification())
//                .matchingTimes(profileRequestDto.getMatchingTimes())
//                .pics(filePath)
//                .prize(profileRequestDto.getPrize())
//                .trainer(User.builder().id(Id).build())
//                .build();
//
//        TrainerProfileResponseDto result = new TrainerProfileResponseDto(trainerProfileRepository.save(saveProfile).getId());
//        return result;
//    }
//
//    //약력 수정
//    public TrainerProfileModifiyResponseDto profileModify(TrainerProfileModifyRequestDto dto, Long Id){
//        Optional<TrainerProfile> findProfileOp = trainerProfileRepository.findById(dto.getProfileId());
//        TrainerProfile findProfile = findProfileOp.get();
//
//        TrainerProfile saveProfile = TrainerProfile.builder()
//                .matchingTimes(findProfile.getMatchingTimes())
//                .avgSarScore(findProfile.getAvgSarScore())
//                .name(findProfile.getName())
//                .certification(dto.getCertification())
//                .pics(dto.getPics())
//                .prize(dto.getPrize())
//                .description(dto.getDescription())
//                .id(findProfile.getId())
//                .trainer(findProfile.getTrainer())
//                .build();
//
//        TrainerProfile resultProfile = trainerProfileRepository.save(saveProfile);
//        return TrainerProfileModifiyResponseDto.builder().profileId(resultProfile.getId()).build();
//    }
//
//    //약력 조회
//    public TrainerProfileGetResponseDto profileGet(Long id){
//
//        Optional<TrainerProfile> optFindProfile = trainerProfileRepository.findById(id);
//
//        // profile 이 없는 경우는 throw 처리
//        if(!optFindProfile.isPresent()){
//            throw new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY);
//        }
//
//        // optional 은 get() 방식으로 데이터 받기
//        TrainerProfile findProfile = optFindProfile.get();
//
//        TrainerProfileGetResponseDto resultDto = TrainerProfileGetResponseDto.builder()
//                .id(findProfile.getId())
//                .avgSarScore(findProfile.getAvgSarScore())
//                .certification(findProfile.getCertification())
//                .description(findProfile.getDescription())
//                .matchingTimes(findProfile.getMatchingTimes())
//                .name(findProfile.getName())
//                .pics(findProfile.getPics())
//                .trainer(findProfile.getTrainer())
//                .prize(findProfile.getPrize())
//                .build();
//
//        return resultDto;
//    }
}
