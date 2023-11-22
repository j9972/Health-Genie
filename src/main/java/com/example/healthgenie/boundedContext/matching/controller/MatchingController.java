package com.example.healthgenie.boundedContext.matching.controller;

import com.example.healthgenie.boundedContext.matching.dto.MatchingDto;
import com.example.healthgenie.boundedContext.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;

    // 임시 매칭 생성
    @PostMapping("")// http://localhost:1234/matching
    public ResponseEntity add(@RequestBody MatchingDto dto){

        MatchingDto responseDto = matchingService.add(dto);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
