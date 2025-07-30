package com.demo.advanced.service;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;

public interface AuditService {
    void processEvent(RateLimitEvent event);
    void processEvent(TransactionEvent event);
}
