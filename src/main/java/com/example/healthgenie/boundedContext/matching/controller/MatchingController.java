package com.example.healthgenie.boundedContext.matching.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matchings")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<Result> save(@RequestBody MatchingRequest request) {
        MatchingResponse response = matchingService.save(
                        request.getUserId(), request.getTrainerId(),
                        request.getDate(), request.getTime(),
                        request.getPlace(), request.getDescription());

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{matchingId}")
    public ResponseEntity<Result> findOne(@PathVariable Long matchingId) {
        MatchingResponse response = matchingService.findOne(matchingId);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(MatchingCondition condition) {
        List<MatchingResponse> responses = matchingService.findAll(condition);

        return ResponseEntity.ok(Result.of(responses));
    }

    @PatchMapping("/{matchingId}")
    public ResponseEntity<Result> update(@PathVariable Long matchingId, @RequestBody MatchingRequest request) {
        MatchingResponse response = matchingService.update(matchingId, request.getState());

        return ResponseEntity.ok(Result.of(response));
    }
}
