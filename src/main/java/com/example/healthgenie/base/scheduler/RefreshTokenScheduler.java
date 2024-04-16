package com.example.healthgenie.base.scheduler;

import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredRefreshToken() {
        long before = refreshTokenRepository.count();
        refreshTokenRepository.deleteAllByExpirationBefore(LocalDateTime.now());
        long after = refreshTokenRepository.count();
        log.info("[SCHEDULER WORKED] : Deleted Token Count={}", before-after);
    }
}
