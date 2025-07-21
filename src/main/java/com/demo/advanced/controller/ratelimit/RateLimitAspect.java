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
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitConfig rateLimitConfig;
    private final ProxyManager<String> proxyManager;

    @Around("@annotation(com.demo.advanced.annotations.RateLimited)")
    public Object rateLimitAccount(ProceedingJoinPoint joinPoint) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String accountId = pathVariables.get("accountId");

        final Bucket bucket = getOrCreateBucket(accountId);

        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        }

        final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        throw new RateLimitException(String.format(RateLimitException.RATE_LIMIT, rateLimitConfig.getCapacity(), accountId));
    }

    /**
     * Obtiene un bucket de Redis para el ID de la cuenta.
     * Si no existe, lo crea usando la configuración de 'rateLimitConfig'.
     */
    private Bucket getOrCreateBucket(String accountId) {
        return proxyManager.builder().build(accountId, rateLimitConfig::bucketConfiguration);
    }
}