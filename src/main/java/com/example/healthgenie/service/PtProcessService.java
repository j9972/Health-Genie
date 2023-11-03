package com.example.healthgenie.service;

import com.example.healthgenie.repository.PtProcessRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PtProcessService {
    private final PtProcessRepository ptProcessRepository;
    private final UserRepository userRepository;

//    /*
//        pt 기록물은 trainer 에 의해서만 작성되지만, 조회는 둘다 가능하게 만들어야 한다
//     */
//    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto, Long trainerId){
//
//        User trainer = userRepository.findByRoleAndId(dto.getRole(),dto.getTrainerId());
//        if(trainer == null){
//            throw new PtProcessException(PtProcessErrorResult.TRAINER_EMPTY);
//        }
//
//        User user = userRepository.findByRoleAndId(dto.getRole(),dto.getUserId());
//        if(user == null){
//            throw new PtProcessException(PtProcessErrorResult.USER_EMPTY);
//        }
//
//        //dto
//        PtProcess ptProcess = PtProcess.builder()
//                .member(User.builder().id(dto.getUserId()).build())
//                .trainer(User.builder().id(trainerId).build())
//                .date(dto.getDate())
//                .ptTimes(dto.getPtTimes())
//                .bodyState(dto.getBodyState())
//                .bmi(dto.getBmi())
//                .weakness(dto.getWeakness())
//                .strength(dto.getStrength())
//                .ptComment(dto.getPtComment())
//                .ptStartDate(dto.getPtStartDate())
//                .ptEndTimes(dto.getPtEndTimes())
//                .build();
//
//        PtProcess result = userPtProcessRepository.save(ptProcess);
//
//        return PtProcessResponseDto.builder()
//                .processId(result.getId()).build();
//    }
//
//    // 트레이너에 관련된 모든 유저에 대한 pt record 가져오기
//    public List<PtProcessListResponseDto> getPtProcessListByTrainer(Long trainerId) {
//
//        List<PtProcess> getProcessList = userPtProcessRepository.getAllByTrainer(User.builder().id(trainerId).build());
//        List<PtProcessListResponseDto> resultList = new ArrayList<>();
//
//        if(getProcessList.isEmpty()){
//            return resultList;
//        }
//
//        return getProcessList.stream()
//                .map(m -> new PtProcessListResponseDto(m.getId(), m.getPtTimes(), m.getDate(), m.getPtStartDate(),
//                        m.getPtEndTimes(), m.getPtComment(), m.getBodyState(), m.getBmi(), m.getWeakness(), m.getStrength()))
//                .collect(Collectors.toList());
//
//    }
//
//    public PtProcessDetailResponseDto getPtProcessDetail(Long processId) {
//
//        PtProcess result = userPtProcessRepository.findsById(processId);
//        return toDetailResponse(result);
//    }
//
//    public PtProcessDetailResponseDto toDetailResponse(PtProcess process){
//        return PtProcessDetailResponseDto.builder()
//                .id(process.getId())
//                .ptTimes(process.getPtTimes())
//                .date(process.getDate())
//                .ptStartDate(process.getPtStartDate())
//                .ptEndTimes(process.getPtEndTimes())
//                .ptComment(process.getPtComment())
//                .trainerId(process.getTrainer().getId())
//                .userId(process.getMember().getId())
//                .bodyState(process.getBodyState())
//                .bmi(process.getBmi())
//                .weakness(process.getWeakness())
//                .strength(process.getStrength())
//                .build();
//    }
//
//    public PtProcessResponseDto updatePtProcess(PtProcessRequestDto dto, Long trainerId) {
//
//        User trainer = userRepository.findByRoleAndId(dto.getRole(),trainerId);
//        if(trainer == null){
//            throw new PtProcessException(PtProcessErrorResult.TRAINER_EMPTY);
//        }
//
//
//    }
//
//    public PtProcessResponseDto deletePtProcess(Long processId, Long trainerId) {
//
//        // findById 저거면 존재의 유무지, 트레이너인지 회원인지 구분이 가나?
//        Optional<User> trainer = userRepository.findById(trainerId);
//        if(trainer == null){
//            throw new PtProcessException(PtProcessErrorResult.TRAINER_EMPTY);
//        }
//
//        Optional<PtProcess> record = userPtProcessRepository.findById(processId);
//
//        if (record == null) {
//            throw new PtProcessException(PtProcessErrorResult.RECORD_EMPTY);
//        } else {
//            PtProcessResponseDto result = userPtProcessRepository.deleteById(processId);
//            return result;
//        }
//
//    }
}
