package com.example.healthgenie.boundedContext.trainer.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer/profile")
public class TrainerProfileController {

    private final TrainerProfileService profileService;
    private final TrainerProfileTransactionService trainerProfileTransactionService;

    // 트레이너 패킷 세부 내용 작성 API
    @PostMapping("/write") // https://localhost:1234/trainer/profile/write
    public ResponseEntity<Result> save(ProfileRequestDto dto) throws IOException {
        ProfileResponseDto response = trainerProfileTransactionService.save(dto);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 본인의 내용
    @GetMapping("/detail/{profileId}") // https://localhost:1234/trainer/profile/{profileId}
    public ResponseEntity<Result> getProfile(@PathVariable Long profileId){
        ProfileResponseDto response = profileService.getProfile(profileId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에서 트레이너 본인 내용을 수정
    @PostMapping("/update/{profileId}") // https://localhost:1234/trainer/profile/update/{profileId}
    public ResponseEntity<Result> updateProfile(@PathVariable Long profileId, ProfileRequestDto dto) throws IOException {
        ProfileResponseDto response = trainerProfileTransactionService.update(profileId, dto);

        return ResponseEntity.ok(Result.of(response));
    }
}
