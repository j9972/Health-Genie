package com.example.healthgenie.boundedContext.trainer.profile.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileDeleteResponseDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileSliceResponse;
import com.example.healthgenie.boundedContext.trainer.profile.service.ProfileService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainers/profiles")
public class ProfileController {

    private final ProfileService profileService;

    // 트레이너 패킷 세부 내용 작성 API
    @PostMapping
    public ResponseEntity<Result> createProfile(@AuthenticationPrincipal User user,
                                                @RequestBody @Valid ProfileRequestDto dto) {

        ProfileResponseDto response = profileService.save(user, dto);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 전체 조회 -> 무한 스크롤
    @GetMapping
    public ResponseEntity<Result> getAllProfile(@RequestParam(value = "lastIndex", required = false) Long lastIndex) {

        List<ProfileResponseDto> response = profileService.getAllProfile(lastIndex);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 다른 트레이너 내영 보냄
    @GetMapping("/details/{profileId}")
    public ResponseEntity<Result> getProfile(@PathVariable Long profileId) {
        ProfileResponseDto response = profileService.getProfile(profileId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 다른 트레이너 내영 보냄
    @GetMapping("/details")
    public ResponseEntity<Result> getProfile(@AuthenticationPrincipal User user) {
        ProfileResponseDto response = profileService.getOwnProfile(user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에서 트레이너 본인 내용을 수정
    @PatchMapping("/{profileId}")
    public ResponseEntity<Result> updateProfile(@PathVariable Long profileId,
                                                @RequestBody @Valid ProfileRequestDto dto,
                                                @AuthenticationPrincipal User user) {

        ProfileResponseDto response = profileService.updateProfile(dto, profileId, user);

        return ResponseEntity.ok(Result.of(response));
    }


    // 프로을 검색으로 찾기
    @GetMapping("/searching")
    public ResponseEntity<Result> searchProfile(@RequestParam(name = "search", defaultValue = "") String name,
                                                @RequestParam(name = "lastId", required = false) Long lastId,
                                                Pageable pageable) {
        ProfileSliceResponse response = ProfileSliceResponse.of(profileService.findAll(name, lastId, pageable));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Result> deleteProfile(@AuthenticationPrincipal User user, @PathVariable Long profileId) {
        ProfileDeleteResponseDto response = profileService.deleteProfile(profileId, user);
        return ResponseEntity.ok(Result.of(response));
    }
}
