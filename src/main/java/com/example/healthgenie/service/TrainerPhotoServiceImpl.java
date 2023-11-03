package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import com.example.healthgenie.repository.TrainerPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TrainerPhotoServiceImpl implements TrainerPhotoService{

    private final TrainerPhotoRepository trainerPhotoRepository;
    private final TrainerProfileService trainerProfileService;

    public void trainerPhotoSaveAll(Collection<TrainerPhoto> photoList){
        trainerPhotoRepository.saveAll(photoList);
    }

    public  List<TrainerPhoto> findAllPhotoTrainerId(Long Id){
        return trainerPhotoRepository.findAllByTrainerId(Id);
    }
/*
    public TrainerPhotoModify(Long id){

        //기존 사진디비리스트 조회
        List<TrainerPhoto> findCurPhotoList =findAllPhotoTrainerId(dto.getId());

        //기존사진없으면 새로운 사진 전체 저장 후 빠른 리턴
        int selectListSize = CommonService.collectionSizeCheck(findCurPhotoList);
        if(selectListSize == 0){
            List<TrainerPhoto> newImageList = new LinkedList<>();
            trainerProfileService.fileListToTrainerPhotoList(newImageList,fileList,info.getMember());
            trainerPhotoSaveAll(newImageList);
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
    }

 */
}
