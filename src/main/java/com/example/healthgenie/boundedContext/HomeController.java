package com.example.healthgenie.boundedContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck() {
        log.info("Load Balancer Health Check!!");
        return ResponseEntity.ok("200");
    }
}
