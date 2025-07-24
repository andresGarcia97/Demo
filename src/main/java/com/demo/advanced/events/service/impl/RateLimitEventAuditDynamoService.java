package com.demo.advanced.events.service.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dynamodb.mapper.RateLimitEventAuditMapper;
import com.demo.advanced.dynamodb.repository.RateLimitEventAuditRepository;
import com.demo.advanced.events.service.RateLimitEventAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("rateLimitEventAuditDynamoService")
@RequiredArgsConstructor
public class RateLimitEventAuditDynamoService implements RateLimitEventAuditService {
    private final RateLimitEventAuditRepository repository;
    private final RateLimitEventAuditMapper mapper;

    @Override
    public void processEvent(RateLimitEvent event) {
        log.info("Saving RateLimitEvent to Dynamo: {}", event.getKey());
        repository.save(mapper.toEntity(event));
    }
}