package com.demo.advanced.events.service.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.events.service.RateLimitEventAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("rateLimitEventAuditLogService")
public class RateLimitEventAuditLogService implements RateLimitEventAuditService {
    @Override
    public void processEvent(RateLimitEvent event) {
        log.info("RateLimitEvent (log only): {}", event.getKey());
    }
}
