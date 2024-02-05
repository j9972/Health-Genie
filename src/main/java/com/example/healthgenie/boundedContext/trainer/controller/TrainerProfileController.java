package com.example.healthgenie.boundedContext.trainer.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileTransactionService;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainer/profiles")
public class TrainerProfileController {

    private final TrainerProfileService profileService;
    private final TrainerProfileTransactionService trainerProfileTransactionService;

    // 트레이너 패킷 세부 내용 작성 API
    @PostMapping
    public ResponseEntity<Result> save(ProfileRequestDto dto, @AuthenticationPrincipal User user) throws IOException {
        log.info("trainer profile controller add -> principal user : {}", user);

        ProfileResponseDto response = trainerProfileTransactionService.save(dto, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 본인의 내용
    @GetMapping("/detail/{profileId}")
    public ResponseEntity<Result> getProfile(@PathVariable Long profileId) {
        ProfileResponseDto response = profileService.getProfile(profileId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에서 트레이너 본인 내용을 수정
    @PatchMapping("/{profileId}")
    public ResponseEntity<Result> updateProfile(@PathVariable Long profileId,
                                                ProfileRequestDto dto,
                                                @AuthenticationPrincipal User user) throws IOException {
        log.info("trainer profile controller update -> principal user : {}", user);
        ProfileResponseDto response = trainerProfileTransactionService.update(profileId, dto, user);

        return ResponseEntity.ok(Result.of(response));
    }
}
