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
public class AccountRateLimit implements RateLimitStrategy {

    private final ProxyManager<String> proxyManager;
    private final EventPublisher eventPublisher;

    @Value("${application.account.rate-limit.capacity:10}")
    private long capacity;

    @Value("${application.account.rate-limit.refillRate:2}")
    private long refill;

    @Value("${application.account.rate-limit.timeInMinutes:1}")
    private long timeInMinutes;

    @Override
    public RateLimitType getType() {
        return RateLimitType.ACCOUNT;
    }

    @Override
    public BucketConfiguration bucketConfig() {

        final Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(refill, Duration.ofMinutes(timeInMinutes))
                .build();

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public Bucket getOrCreateBucket(String accountId) {
        return proxyManager.builder().build(accountId, this::bucketConfig);
    }

    @Override
    public Object handleLimit(ProceedingJoinPoint joinPoint) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final String accountKey = getKeyForBucket(request, "accountId");
        final Bucket bucketAccount = getOrCreateBucket(accountKey);

        if (bucketAccount.tryConsume(1)) {
            return joinPoint.proceed();
        }

        eventPublisher.publishRateLimitEvent(accountKey);

        final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        throw new RateLimitException(RateLimitException.RATE_LIMIT);
    }
}