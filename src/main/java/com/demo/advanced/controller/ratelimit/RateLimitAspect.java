package com.demo.advanced.controller.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final Map<RateLimitType, RateLimitStrategy> strategies;

    public RateLimitAspect(final List<RateLimitStrategy> strategyList) {
        this.strategies = strategyList.stream().collect(Collectors.toMap(RateLimitStrategy::getType, Function.identity()));
    }

    @Around("@annotation(com.demo.advanced.controller.ratelimit.RateLimited)")
    public Object rateLimitAccount(ProceedingJoinPoint joinPoint) throws Throwable {

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final RateLimited rateLimitedAnnotation = method.getAnnotation(RateLimited.class);

        final RateLimitType type = rateLimitedAnnotation.value();
        final RateLimitStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }

        return strategy.handleLimit(joinPoint);
    }

}