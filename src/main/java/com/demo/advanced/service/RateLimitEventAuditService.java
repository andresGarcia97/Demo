package com.demo.advanced.service;

import com.demo.advanced.dto.event.RateLimitEvent;

public interface RateLimitEventAuditService {
    void processEvent(RateLimitEvent event);
}
