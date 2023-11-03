package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.dto.*;
import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.*;
import com.example.healthgenie.global.constant.FileUrl;
import com.example.healthgenie.repository.TrainerInfoRepository;
import com.example.healthgenie.repository.TrainerPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TrainerProfileServiceImpl implements TrainerProfileService {
    private final TrainerInfoRepository trainerProfileRepository;
    private final fileService fileService;
    private final TrainerPhotoService trainerPhotoService;
    //private final CommonService commonService;
    //trainer와 onetoone관계 - 트레이너Id넣고 save하면 같은거 찾아서 업데이트함
    //약력작성 전에 trainer정보를 미리 보내놓기 때문에 trainer정보가 틀릴일이 없다.
    //트레이너 정보를 보내는 과정에서 트레이너 id가 틀릴 경우가 검증되어있음 그냥 save하면 된다.

    @Override
    public TrainerInfo getProfile(Long id){
        Optional<TrainerInfo> trainerInfo = trainerProfileRepository.findById(id);
        if(trainerInfo.isPresent()){
            return trainerInfo.get();
        }
        else{
            return null;
        }
    }
    //약력 작성
    @Override
    public Long profileWrite(TrainerProfileRequestDto profileRequestDto,
                             MultipartFile profile, List<MultipartFile> photo,User user){

        Long returnid=null;
        TrainerInfo info = getProfile(profileRequestDto.getId());
        boolean nullProfile = CommonService.isNull(profile);//null true
        boolean nullphoto = photo.isEmpty();//null true
        if(info==null){//추가
            returnid = profileAddPhotoAndProfile(profileRequestDto,profile,photo,user);

        }
        else{//수정
            returnid = profileModifyPhotoAndProfile(profileRequestDto,profile,photo,info);
        }
        return returnid;
        //TrainerProfileResponseDto result = new TrainerProfileResponseDto(trainerProfileRepository.save(saveProfile).getId());
        //return result;
    }
    @Override
    @Transactional
    public Long profileAddPhotoAndProfile(TrainerProfileRequestDto dto,MultipartFile profile,List<MultipartFile> file,User user){
        String profileUrl = null;

        if(profile != null){
            profileUrl = fileService.saveImage(profile, FileUrl.TRAINER_PROFILE);
        }
        if(profileUrl !=null){
            TrainerInfo trainerInfo = TrainerInfo.profileDtoToEntity(dto,profileUrl);
            trainerProfileRepository.save(trainerInfo).getId();
        }
        //2. 상세 사진 저장
        if(file.isEmpty()){
            return 0L;
        }
        List<TrainerPhoto> photoList = fileService.saveImageList(file,FileUrl.TRAINER_PHOTO, user);
        trainerPhotoService.trainerPhotoSaveAll(photoList);
        //3. 사진 저장경로 및 등등 해서 트레이너정보엔티티 생성/저장
        return 1L;
    }

    //반복적으로 file to TrainerPhoto list add하자 그냥
    //약력 수정
    @Override
    @Transactional
    public Long profileModifyPhotoAndProfile(TrainerProfileRequestDto dto,
                              MultipartFile profile, List<MultipartFile> fileList,TrainerInfo info){

        //1. 기존프로필(실물) 디렉토리에서 삭제 후 새로운 프로필 저장
        if(profile != null){
            String ModifyProfileUrl = fileService.modifyImage(info.getProfilePhoto(),profile,FileUrl.TRAINER_PROFILE);
            info.changeProfilePhotoUrl(ModifyProfileUrl);
        }

        //2. 프로필 디비 수정
        info.modifyInfo(dto);

        //2. 기존db와 변경된 사진List비교 후 db수정 후 db에 맞게 실물 사진 변경 기존상세사진(실물) 삭제 후 새로운 상세자진 저장, 트레이너포토전체 삭제

        //기존 사진디비리스트 조회
        List<TrainerPhoto> findCurPhotoList =trainerPhotoService.findAllPhotoTrainerId(dto.getId());

        //기존사진없으면 새로운 사진 전체 저장 후 빠른 리턴
        int selectListSize = CommonService.collectionSizeCheck(findCurPhotoList);
        if(selectListSize == 0){
            List<TrainerPhoto> newImageList = new LinkedList<>();
            fileListToTrainerPhotoList(newImageList,fileList,info.getMember());
            trainerPhotoService.trainerPhotoSaveAll(newImageList);
            return 1L;
        }
        //기존사진이 있을 경우
        Set<String> findAllPhotoStringSet = findCurPhotoList.stream().map(TrainerPhoto::getFilename).collect(Collectors.toSet());
        //중복 찾아서 삭제하기
        //filelist = 새로받은 포토리스트
        //findCurPhotoList = 기존 포토리스트
        //기존리스트는 삭제하고 없어짐 ->고영속성땜에
        for(String loopCurPhoto : findAllPhotoStringSet) {
            for (MultipartFile file : fileList) {
                if (loopCurPhoto == file.getName()) {
                    fileList.remove(file);
                    findAllPhotoStringSet.remove(loopCurPhoto);
                    break;
                }
            }
        }
        for(TrainerPhoto loopCurPhoto : findCurPhotoList) {
            if(findAllPhotoStringSet.contains(loopCurPhoto.getFilename())) {
                findCurPhotoList.remove(loopCurPhoto);
            }
        }
        List<TrainerPhoto> newImageList = new LinkedList<>();
        fileListToTrainerPhotoList(newImageList,fileList,info.getMember());
        trainerPhotoService.trainerPhotoSaveAll(newImageList);
        //3. 기제 프로필-> 새로운프로필로 정보변경, 프로필경로 변경
        return 1L;
    }
    @Override
    public void fileListToTrainerPhotoList(List<TrainerPhoto> newImageList, List<MultipartFile> fileList, User user){
        for(MultipartFile file : fileList) {
            newImageList.add(
                    TrainerPhoto.fileToTrainerPhoto(user,fileService.nameingFile(file,FileUrl.TRAINER_PHOTO).getAbsolutePath()));
        }
    }

    public Page<TrainerSimpleInfo> getProfileByPage(int page, int size, String sortType) {
        if(page<0){
            throw new CommunityPostException(CommunityPostErrorResult.PAGE_EMPTY);
        }
        Sort sort;
        if(sortType.equals("popular")){
            sort = Sort.by("review_avg").descending();
        }
        else if(sortType.equals("new")){
            sort = Sort.by("createdDate").descending();
        }
        else{
            //예외처리할 수도 있고 기본값으로 popular로 갈수도 //예외처리하자
            log.error("error sortType not equls");
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }
        //Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TrainerInfo> trainerProfiles = trainerProfileRepository.findAll(pageable);

        if(trainerProfiles.getTotalElements() == 0 ){
            log.info("조건에 맞는 트레이너가 없음");
        }
        Page<TrainerSimpleInfo> result = new TrainerSimpleInfo().toDtoPage(trainerProfiles);
        return result;
    }
}
