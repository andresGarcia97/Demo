package com.demo.advanced.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Getter
    @Value("${application.rate-limit.capacity:10}")
    private long capacity;

    @Value("${application.rate-limit.refillRate:2}")
    private long refillRate;

    @Value("${application.rate-limit.timeInMinutes:1}")
    private long timeInMinutes;

    @Bean
    public RedisClient redisClient(@Value("${spring.data.redis.host}") String redisHost, @Value("${spring.data.redis.port}") int redisPort) {
        return RedisClient.create(RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withSsl(false)
                .build()
        );
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(RedisClient redisClient) {
        final StatefulRedisConnection<String,byte[]> redisConnection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        return LettuceBasedProxyManager
                .builderFor(redisConnection)
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {

        final Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(refillRate, Duration.ofMinutes(timeInMinutes))
                .build();

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }

}