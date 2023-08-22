package com.example.healthgenie.controller;

import com.example.healthgenie.domain.trainer.dto.*;
import com.example.healthgenie.service.FileService;
import com.example.healthgenie.service.TrainerProfileServie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class TrainerProfileController {


    private final TrainerProfileServie profileServie;
    private final FileService fileService;

//<<<<<<< Updated upstream

//    @PostMapping("/add") // http://localhost:1234/api/v1/profile/add
//    public ResponseEntity profileAdd(@RequestBody TrainerProfileRequestDto dto){
//=======
//    @PostMapping("/addtest")
//    public ResponseEntity profileAdd(@RequestPart("info") TrainerProfileRequestDto dto, @RequestPart("file") MultipartFile file){
//
//        Long userId =1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체
//        System.out.println(dto);
//        System.out.println(file.getOriginalFilename());
//
//        String filePath = fileService.trainerProfileAdd(file);
//        TrainerProfileResponseDto result = profileServie.profileAdd(dto,userId,filePath);
//        return new ResponseEntity("result", HttpStatus.OK);
//    }
    @PostMapping("/add")
    public ResponseEntity profileAdd(@RequestPart("info") TrainerProfileRequestDto dto, @RequestPart("file") MultipartFile file){
//>>>>>>> Stashed changes

        Long userId =1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체

        String filePath = fileService.trainerProfileAdd(file);
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
}
