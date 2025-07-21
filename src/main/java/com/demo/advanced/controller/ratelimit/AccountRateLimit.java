package com.demo.advanced.controller.ratelimit;

import com.demo.advanced.config.RateLimitConfig;
import com.demo.advanced.exception.RateLimitException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRateLimit implements RateLimitStrategy {

    private final RateLimitConfig rateLimitConfig;
    private final ProxyManager<String> proxyManager;

    @Override
    public RateLimitType getType() {
        return RateLimitType.ACCOUNT;
    }

    @Override
    public Bucket getOrCreateBucket(String accountId) {
        return proxyManager.builder().build(accountId, rateLimitConfig::bucketConfiguration);
    }

    @Override
    public Object handleLimit(ProceedingJoinPoint joinPoint) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final String accountKey = getKeyForBucket(request, "accountId");
        final Bucket bucketAccount = getOrCreateBucket(accountKey);

        if (bucketAccount.tryConsume(1)) {
            return joinPoint.proceed();
        }

        final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        throw new RateLimitException(String.format(RateLimitException.RATE_LIMIT, rateLimitConfig.getCapacity()));
    }
}