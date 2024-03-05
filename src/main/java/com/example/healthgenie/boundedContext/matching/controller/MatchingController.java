package com.example.healthgenie.boundedContext.matching.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.service.MatchingService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matchings")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<Result> save(@AuthenticationPrincipal User trainer, @RequestBody MatchingRequest request) {
        MatchingResponse response = MatchingResponse.of(matchingService.save(trainer, request));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{matchingId}")
    public ResponseEntity<Result> findById(@PathVariable Long matchingId, @AuthenticationPrincipal User user) {
        MatchingResponse response = MatchingResponse.of(matchingService.findById(matchingId, user));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@AuthenticationPrincipal User user, @RequestBody MatchingCondition condition) {
        List<MatchingResponse> responses = MatchingResponse.of(matchingService.findAll(user, condition));

        return ResponseEntity.ok(Result.of(responses));
    }

    @PatchMapping("/{matchingId}")
    public ResponseEntity<Result> update(@PathVariable Long matchingId,
                                         @AuthenticationPrincipal User user,
                                         @RequestBody MatchingRequest request) {
        MatchingResponse response = MatchingResponse.of(matchingService.update(matchingId, user, request.getState()));

        return ResponseEntity.ok(Result.of(response));
    }
}
