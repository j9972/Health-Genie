package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.utils.JwtUtils;
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
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("message={}", message);

        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT == accessor.getCommand()) {
            final String accessToken = jwtUtils.resolveToken(accessor.getFirstNativeHeader("Authorization"));
            log.info("CONNECT {}", accessToken);

//            jwtUtils.validateToken(accessToken);
        }

        return message;
    }
}
