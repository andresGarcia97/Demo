package com.demo.advanced.controller.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

public interface RateLimitStrategy {

    RateLimitType getType();
    BucketConfiguration bucketConfig();
    Bucket getOrCreateBucket(String key);
    Object handleLimit(ProceedingJoinPoint joinPoint) throws Throwable;

    default String getKeyForBucket(final HttpServletRequest request, String parameterKey) {
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String parameter = pathVariables.get(parameterKey);
        return this.getType().name().concat("-").concat(parameter);
    }

}
