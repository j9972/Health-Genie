package com.example.healthgenie.base.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    // Redis 서버와의 연결 정보를 저장하는 객체
    private final RedisProperties redisProperties;

    // RedisProperties로 yaml에 저장한 host, post를 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(
//            RedisConnectionFactory connectionFactory,
//            MessageListenerAdapter listenerAdapter,
//            ChannelTopic channelTopic
//    ) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, channelTopic);
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
//        return new MessageListenerAdapter(subscriber, "onMessage");
//    }
//
//    @Bean
//    public ChannelTopic channelTopic() {
//        return new ChannelTopic("chatRoom");
//    }
}