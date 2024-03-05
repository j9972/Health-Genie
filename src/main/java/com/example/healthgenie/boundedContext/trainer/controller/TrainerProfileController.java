package com.example.healthgenie.boundedContext.trainer.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileTransactionService;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        ProfileResponseDto response = trainerProfileTransactionService.save(dto, user);
        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> getAllProfile(@RequestParam(required = false, defaultValue = "0") int page) {

        List<ProfileResponseDto> response = profileService.getAllProfile(page, 10);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 본인 내용
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

        ProfileResponseDto response = trainerProfileTransactionService.update(profileId, dto, user);

        return ResponseEntity.ok(Result.of(response));
    }

    // 후기를 검색으로 찾기
    @GetMapping("/searching")
    public ResponseEntity<Result> searchProfile(@RequestParam(name = "search", defaultValue = "") String name) {
        List<ProfileResponseDto> response = profileService.findAll(name);

        return ResponseEntity.ok(Result.of(response));
    }
}
