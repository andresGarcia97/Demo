package com.demo.advanced.service.decorator.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(
        name = "featureflag.audit.dynamo",
        havingValue = "false",
        matchIfMissing = true
)
public class AuditLogService implements AuditService {

    @Override
    public void processEvent(final RateLimitEvent event) {
        log.info("RateLimitEvent (log only): {}", event.getKey());
    }

    @Override
    public void processEvent(final TransactionEvent event) {
        log.info("TransactionEvent (log only): {}", event);
    }

}
