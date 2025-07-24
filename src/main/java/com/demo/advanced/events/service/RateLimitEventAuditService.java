package com.demo.advanced.events.service;

import com.demo.advanced.dto.event.RateLimitEvent;

public interface RateLimitEventAuditService {
    void processEvent(RateLimitEvent event);
}
