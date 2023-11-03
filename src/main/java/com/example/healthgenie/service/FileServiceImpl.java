package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.repository.TrainerInfoRepository;
import com.example.healthgenie.repository.TrainerPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Log4j2
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements fileService{

    @Override
    public List<TrainerPhoto> saveImageList(List<MultipartFile> imagefile, String url, User user) {

        List<TrainerPhoto> photoList = new LinkedList<>();
        try{
            for(MultipartFile loopFile : imagefile){
                File file = nameingFile(loopFile,url);

                loopFile.transferTo(file);
                photoList.add(TrainerPhoto.builder()
                        .filename(file.getAbsolutePath())
                        .trainer(user)
                        .build());
            }
        }catch (Exception e){
        }
        return photoList;
    }

    @Override
    public File nameingFile(MultipartFile file, String url){

        String fileRealName = file.getOriginalFilename(); //파일명을 얻어낼 수 있는 메서드!
        long size = file.getSize(); //파일 사이즈

        System.out.println("파일명 : "  + fileRealName);
        //서버에 저장할 파일이름 fileextension으로 .jsp이런식의  확장자 명을 구함
        String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());
		/*
		  파일 업로드시 파일명이 동일한 파일이 이미 존재할 수도 있고 사용자가
		  업로드 하는 파일명이 언어 이외의 언어로 되어있을 수 있습니다.
		  타인어를 지원하지 않는 환경에서는 정산 동작이 되지 않습니다.(리눅스가 대표적인 예시)
		  고유한 랜던 문자를 통해 db와 서버에 저장할 파일명을 새롭게 만들어 준다.
		 */

        UUID uuid = UUID.randomUUID();
        String[] uuids = uuid.toString().split("-");

        String uniqueName = uuids[0];
        System.out.println("생성된 고유문자열" + uniqueName);
        System.out.println("확장자명" + fileExtension);

        File namedFile = new File(url+"\\"+uniqueName + fileExtension);  // 적용 후
        return namedFile;
    }
    public String saveImage(MultipartFile namedFile, String url) {

        File saveFile = nameingFile(namedFile,url);

        try {
            namedFile.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveFile.getAbsolutePath();
    }

    @Override
    public void displayImage(Long Id) {

    }
    //오로지 삭제만 담당

    //있나없나 검사하는 메서드 만들자

    public boolean existsImage(String fileUrlName){
        if(!fileUrlName.equals(null)){
            return true;
        }else{
            return false;
        }
    }

    @Override// id = trainer id url== trainerphoto 디렉토리 경로
    public List<String> modifyImageList(Long Id,List<MultipartFile> fileList,String url){

//error 고쳐야해 return
        List<String> modifyImageList = new LinkedList<>();
        for (MultipartFile file: fileList) {
            modifyImageList.add("삭제");

        }
        return modifyImageList;
    }
    @Override//STring filename = 기존 가지고있는 파일네임(경로+이름), multipartFile 수정할 새로운 파일(이름)
    public String modifyImage(String fileUrlName,MultipartFile file,String url){
        if(existsImage(fileUrlName)){
            deleteImage(fileUrlName);
        }
        String savedUrl = saveImage(file,url);

        return savedUrl;
    }
    @Override
    public boolean deleteImage(String fileUrlName) {
        File file = new File(fileUrlName);
        boolean delResult = file.delete();
        return delResult;
    }

}
