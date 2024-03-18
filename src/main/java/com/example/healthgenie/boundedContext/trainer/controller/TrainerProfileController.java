package com.example.healthgenie.boundedContext.trainer.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileSliceResponse;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainers/profiles")
public class TrainerProfileController {

    private final TrainerProfileService profileService;

    // 트레이너 패킷 세부 내용 작성 API
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Result> createProfile(@AuthenticationPrincipal User user,
                                                @RequestPart ProfileRequestDto dto,
                                                @RequestPart(name = "profileImages", required = false) List<MultipartFile> profileImages) {

        ProfileResponseDto response = profileService.save(user, dto, profileImages);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 전체 조회 -> 무한 스크롤
    @GetMapping
    public ResponseEntity<Result> getAllProfile(@RequestParam(value = "lastIndex", required = false) Long lastIndex) {

        List<ProfileResponseDto> response = profileService.getAllProfile(lastIndex);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에 보여줄 본인 내용
    @GetMapping("/details/{profileId}")
    public ResponseEntity<Result> getProfile(@PathVariable Long profileId) {
        ProfileResponseDto response = profileService.getProfile(profileId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 관리페이지에서 트레이너 본인 내용을 수정
    @PatchMapping(value = "/{profileId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Result> updateProfile(@PathVariable Long profileId,
                                                @RequestPart ProfileRequestDto dto,
                                                @AuthenticationPrincipal User user,
                                                @RequestPart(name = "profileImages", required = false) List<MultipartFile> profileImages) {

        ProfileResponseDto response = profileService.updateProfile(dto, profileId, user, profileImages);

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
}
