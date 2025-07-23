package com.demo.advanced.controller.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Define un pointcut que apunta a cualquier método anotado con @{@link Logging}
     * dentro de los resources anotados con @RestController.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(com.demo.advanced.controller.logging.Logging)")
    public void loggingEntryAndExit() {}

    @Around("loggingEntryAndExit()")
    public Object logMethodEntryAndExit(final ProceedingJoinPoint joinPointRest) throws Throwable {

        final String methodName = joinPointRest.getSignature().toShortString();
        final Object[] methodArgs = joinPointRest.getArgs();
        log.info("{} >> {}", methodName, Arrays.toString(methodArgs));

        final Object result = joinPointRest.proceed();
        log.info("{} << {}", methodName, result);
        return result;

    }
}