package com.demo.advanced.controller.ratelimit;

import com.demo.advanced.events.EventPublisher;
import com.demo.advanced.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientRateLimit implements RateLimitStrategy {

    private final ProxyManager<String> proxyManager;
    private final EventPublisher eventPublisher;

    @Value("${application.client.rate-limit.capacity:15}")
    private long capacity;

    @Value("${application.client.rate-limit.refillRate:1}")
    private long refillRate;

    @Value("${application.client.rate-limit.timeInMinutes:2}")
    private long timeInMinutes;

    @Override
    public RateLimitType getType() {
        return RateLimitType.CLIENT;
    }

    @Override
    public BucketConfiguration bucketConfig() {

        final Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(refillRate, Duration.ofMinutes(timeInMinutes))
                .build();

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public Bucket getOrCreateBucket(String clientId) {
        return proxyManager.builder().build(clientId, this::bucketConfig);
    }

    @Override
    public Object handleLimit(ProceedingJoinPoint joinPoint) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final String clientKey = getKeyForBucket(request, "clientId");

        final Bucket bucketClient = getOrCreateBucket(clientKey);

        if (bucketClient.tryConsume(1)) {
            return joinPoint.proceed();
        }

        eventPublisher.publishRateLimitEvent(clientKey);

        final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        throw new RateLimitException(RateLimitException.RATE_LIMIT);
    }

}