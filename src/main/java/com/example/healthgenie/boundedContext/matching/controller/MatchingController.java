package com.example.healthgenie.boundedContext.matching.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
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
        MatchingResponse response = matchingService.save(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAllByDate(@RequestBody MatchingRequest request) {
        List<MatchingResponse> responses = matchingService.findByDateAndNicknames(request);

        return ResponseEntity.ok(Result.of(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> findOne(@PathVariable Long id, @RequestBody MatchingRequest request) {
        MatchingResponse response = matchingService.findOne(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping
    public ResponseEntity<Result> update(@RequestParam(name = "state") MatchingState state,
                                         @RequestBody MatchingRequest request
    ) {
        switch (state) {
            case PARTICIPATE -> matchingService.participate(request);
            case CANCEL -> matchingService.cancel(request);
            case PARTICIPATE_ACCEPT -> matchingService.participateAccept(request);
            default -> throw new IllegalStateException("wrong state value");
        }

        return ResponseEntity.ok(Result.of(state.getCode() + " 수정 완료"));
    }

    @DeleteMapping
    public ResponseEntity<Result> remove(@RequestBody MatchingRequest request) {
        matchingService.breakup(request);

        return ResponseEntity.ok(Result.of("매칭 취소 완료"));
    }
}
