package com.demo.advanced.service.decorator;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.service.AuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AuditServiceDecorator implements AuditService {

    private final AuditService logService;
    private final AuditService dynamoService;
    private final boolean useDynamo;

    public AuditServiceDecorator(
            @Qualifier("eventAuditLogService") AuditService logService,
            @Qualifier("eventAuditDynamoService") AuditService dynamoService,
            @Value("${featureflag.audit.dynamo:false}") boolean useDynamo) {
        this.logService = logService;
        this.dynamoService = dynamoService;
        this.useDynamo = useDynamo;
    }

    @Override
    public void processEvent(final RateLimitEvent event) {
        if (useDynamo) {
            dynamoService.processEvent(event);
        } else {
            logService.processEvent(event);
        }
    }

    @Override
    public void processEvent(final TransactionEvent event) {
        if (useDynamo) {
            dynamoService.processEvent(event);
        } else {
            logService.processEvent(event);
        }
    }
}