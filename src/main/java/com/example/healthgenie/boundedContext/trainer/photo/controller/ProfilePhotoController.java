package com.example.healthgenie.boundedContext.trainer.photo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.photo.dto.ProfilePhotoRequest;
import com.example.healthgenie.boundedContext.trainer.photo.dto.ProfilePhotoResponse;
import com.example.healthgenie.boundedContext.trainer.photo.service.ProfilePhotoService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers/profiles/{profileId}/photos")
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long profileId, @AuthenticationPrincipal User user,
                                       @Valid ProfilePhotoRequest dto) throws IOException {
        List<ProfilePhotoResponse> response = ProfilePhotoResponse.of(
                profilePhotoService.save(profileId, user.getId(), dto));

        return ResponseEntity.ok(Result.of(response));
    }


}
