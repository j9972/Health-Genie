package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("message={}", message);

        // STOMP 헤더 접근을 위한 accessor
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // STOMP 연결 시 토큰 검증
        if(StompCommand.CONNECT == accessor.getCommand()) {
            final String accessToken = accessor.getFirstNativeHeader("AccessToken");

            jwtTokenProvider.validateToken(accessToken);
        }

        return message;
    }
}
