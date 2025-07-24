package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.events.service.RateLimitEventAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListenerRateLimit implements ApplicationListener<RateLimitEvent> {
    private final RateLimitEventAuditService auditService;

    @Override
    public void onApplicationEvent(final RateLimitEvent event) {
        log.info("Received RateLimitEvent: {}", event.getKey());
        auditService.processEvent(event);
    }
}