package com.example.healthgenie.controller;

import com.example.healthgenie.domain.trainer.dto.*;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.service.FileServiceImpl;
import com.example.healthgenie.service.TrainerProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer/profile")
public class TrainerProfileController {


    private final TrainerProfileServiceImpl trainerProfileServie;
    private final FileServiceImpl fileServiceImpl;


    @PostMapping("/write") // http://localhost:1234/api/v1/profile/add
    public ResponseEntity profileWrite(@AuthenticationPrincipal User user,
                                     @RequestPart("info") TrainerProfileRequestDto dto,
                                     @RequestPart(name = "profile",required = false) MultipartFile profile,
                                     @RequestPart(name = "photo",required = false) List<MultipartFile> photo ) {

        //수정, 작성을 같이
        //이중으로 검사하자
        trainerProfileServie.profileWrite(dto,profile,photo,user);

        return new ResponseEntity("hi",HttpStatus.OK);

    }
    /*
    @PostMapping("/addtest")
    public ResponseEntity profileAdd(@RequestPart("info") TrainerProfileRequestDto dto, @RequestPart("file") List<MultipartFile> file){

        Long userId =1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체
        System.out.println(dto);
        System.out.println(file.getOriginalFilename());

        trainerProfileServie.profileWrite(dto,file);

        String filePath = fileService.trainerProfileAdd(file);
        TrainerProfileResponseDto result = profileServie.profileAdd(dto,userId,filePath);
        return new ResponseEntity("result", HttpStatus.OK);
    }



    @PostMapping("/add")
    public ResponseEntity profileAdd(@RequestPart("info") TrainerProfileRequestDto dto,
                                     @RequestPart("profile") MultipartFile profile,
                                     @RequestPart("photo") List<MultipartFile> photo ){
        Long userId =1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체

        String filePath = fileServiceImpl.trainerProfileAdd(file);
        TrainerProfileResponseDto result = profileServie.profileAdd(dto,userId,filePath);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/modify") // http://localhost:1234/api/v1/profile/modify
    public ResponseEntity profileModifiy(@RequestBody TrainerProfileModifyRequestDto dto){

        Long userId = 1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체

        TrainerProfileModifiyResponseDto result = profileServie.profileModify(dto,userId);

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/{id}") // http://localhost:1234/api/v1/profile/
    public ResponseEntity profileGet(@PathVariable("id") Long id){

        TrainerProfileGetResponseDto result = profileServie.profileGet(id);

        return new ResponseEntity(result,HttpStatus.OK);
    }

     */
}
