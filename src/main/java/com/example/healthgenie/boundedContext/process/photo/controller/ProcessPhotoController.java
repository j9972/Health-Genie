package com.example.healthgenie.boundedContext.process.photo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoDeleteResponse;
import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoRequest;
import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoResponse;
import com.example.healthgenie.boundedContext.process.photo.service.ProcessPhotoService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/process/{processId}/photos")
public class ProcessPhotoController {


    private final ProcessPhotoService photoService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long processId, @AuthenticationPrincipal User user,
                                       @Valid ProcessPhotoRequest dto) throws IOException {
        List<ProcessPhotoResponse> response = ProcessPhotoResponse.of(
                photoService.save(processId, user.getId(), dto));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<Result> findById(@PathVariable Long processId, @PathVariable Long photoId) {
        ProcessPhotoResponse response = ProcessPhotoResponse.of(photoService.findById(photoId));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@PathVariable Long processId) {
        List<ProcessPhotoResponse> response = ProcessPhotoResponse.of(
                photoService.findAllByProcessId(processId));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping
    public ResponseEntity<Result> delete(@PathVariable Long processId, @AuthenticationPrincipal User user) {
        ProcessPhotoDeleteResponse response = photoService.deleteAllByProcessId(
                processId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}
