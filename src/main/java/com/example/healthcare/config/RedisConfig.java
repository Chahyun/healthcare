package com.example.healthcare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    // CacheManager를 구성하는 메서드
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        // Redis 캐시 설정을 구성합니다.
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // RedisCacheManager를 생성하여 반환합니다.
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration).build();
    }

    // Redis 연결 팩토리를 구성하는 메서드
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // RedisStandaloneConfiguration으로 단독 Redis 연결 구성을 생성합니다.
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(this.host); // 호스트 이름 설정
        configuration.setPort(this.port);     // 포트 번호 설정

        // LettuceConnectionFactory를 생성하여 반환합니다.
        return new LettuceConnectionFactory(configuration);
    }
}
