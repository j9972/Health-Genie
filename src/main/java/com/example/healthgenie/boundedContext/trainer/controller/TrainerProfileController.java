package com.example.healthgenie.boundedContext.trainer.controller;

import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer/profile")
public class TrainerProfileController {

    private final TrainerProfileService profileServie;

    /*
        관리페이지 용 트레이너에게 본인 정보에 대한 CRU
        기존에 정보가 있을 수도 있다 ( 해당 4개의 필드에 대해서 update 식으로 생각하면 된다 )
     */
    @GetMapping("/{profileId}") // https://localhost:1234/trainer/profile/{profileId}
    public ResponseEntity getProfile(@PathVariable Long profileId){
        ProfileResponseDto response = profileServie.getProfile(profileId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/write") // https://localhost:1234/trainer/profile/write
    public ResponseEntity writeProfile(@RequestBody ProfileRequestDto dto){
        ProfileResponseDto response = profileServie.writeProfile(dto);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/update/{profileId}") // https://localhost:1234/trainer/profile/update/{profileId}
    public ResponseEntity updateProfile(@RequestBody ProfileRequestDto dto, @PathVariable Long profileId){
        ProfileResponseDto response = profileServie.updateProfile(dto, profileId);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
