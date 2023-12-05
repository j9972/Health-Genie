package com.example.healthgenie.boundedContext.matching.controller;

import com.example.healthgenie.base.response.Result;
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
        MatchingResponse response = matchingService.save(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAllByDate(@RequestBody MatchingRequest request) {
        List<MatchingResponse> responses = matchingService.findAllByDate(request);

        return ResponseEntity.ok(Result.of(responses));
    }

    @PatchMapping("/participate")
    public ResponseEntity<Result> participate(@RequestParam(name = "state") Boolean state,
                                              @RequestBody MatchingRequest request
    ) {
        String response = "";

        if(state) {
            response = matchingService.participate(request);
        } else {
            response = matchingService.cancel(request);
        }

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/accept")
    public ResponseEntity<Result> accept(@RequestParam(name = "state") Boolean state,
                                         @RequestBody MatchingRequest request
    ) {
        String response = "";

        if(state) {
            response = matchingService.accept(request);
        } else {
            response = matchingService.reject(request);
        }

        return ResponseEntity.ok(Result.of(response));
    }
}
