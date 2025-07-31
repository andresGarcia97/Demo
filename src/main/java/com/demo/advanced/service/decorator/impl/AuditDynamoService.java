package com.demo.advanced.service.decorator.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.repository.AuditRepository;
import com.demo.advanced.service.AuditService;
import com.demo.advanced.service.mapper.eventaudit.RateLimitEventAuditMapper;
import com.demo.advanced.service.mapper.eventaudit.TransactionEventAuditMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "featureflag.audit.dynamo",
        havingValue = "true"
)
@RequiredArgsConstructor
public class AuditDynamoService implements AuditService {

    private final AuditRepository repository;
    private final RateLimitEventAuditMapper mapperRateLimit;
    private final TransactionEventAuditMapper mapperTransaction;

    @Override
    public void processEvent(final RateLimitEvent event) {
        repository.save(mapperRateLimit.toEntity(event));
    }

    @Override
    public void processEvent(final TransactionEvent event) {
        repository.save(mapperTransaction.toEntity(event));
    }
}