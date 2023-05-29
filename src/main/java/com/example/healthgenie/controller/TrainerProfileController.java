package com.example.healthgenie.controller;

import com.example.healthgenie.dto.TrainerProfileModifiyResponseDto;
import com.example.healthgenie.dto.TrainerProfileModifyRequestDto;
import com.example.healthgenie.dto.TrainerProfileRequestDto;
import com.example.healthgenie.dto.TrainerProfileResponseDto;
import com.example.healthgenie.service.TrainerProfileServie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerProfileController {


    private final TrainerProfileServie profileServie;


    @PostMapping("/profile/add")
    public ResponseEntity profileAdd(TrainerProfileRequestDto dto){

        Long userId =1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체

        TrainerProfileResponseDto result = profileServie.profileAdd(dto,userId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/profile/modify")
    public ResponseEntity profileModifiy(TrainerProfileModifyRequestDto dto){

        Long userId = 1L;//회원기능 구현 시 userAdapter에서 Id받는 것으로 대체

        TrainerProfileModifiyResponseDto result = profileServie.profileModify(dto,userId);

        return new ResponseEntity(result,HttpStatus.OK);
    }
}
