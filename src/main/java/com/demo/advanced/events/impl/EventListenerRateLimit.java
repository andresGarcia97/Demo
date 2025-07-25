package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.events.service.RateLimitEventAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListenerRateLimit implements ApplicationListener<RateLimitEvent> {

    private final RateLimitEventAuditService auditService;

    @Async
    @Override
    public void onApplicationEvent(final RateLimitEvent event) {
        auditService.processEvent(event);
    }
}