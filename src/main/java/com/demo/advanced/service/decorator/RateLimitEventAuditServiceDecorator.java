package com.demo.advanced.service.decorator;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.service.RateLimitEventAuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class RateLimitEventAuditServiceDecorator implements RateLimitEventAuditService {

    private final RateLimitEventAuditService logService;
    private final RateLimitEventAuditService dynamoService;
    private final boolean useDynamo;

    public RateLimitEventAuditServiceDecorator(
            @Qualifier("rateLimitEventAuditLogService") RateLimitEventAuditService logService,
            @Qualifier("rateLimitEventAuditDynamoService") RateLimitEventAuditService dynamoService,
            @Value("${featureflag.ratelimit.dynamo:false}") boolean useDynamo) {
        this.logService = logService;
        this.dynamoService = dynamoService;
        this.useDynamo = useDynamo;
    }

    @Override
    public void processEvent(RateLimitEvent event) {
        if (useDynamo) {
            dynamoService.processEvent(event);
        } else {
            logService.processEvent(event);
        }
    }
}