package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventListenerGeneral {

    private final AuditService auditService;

    @Async
    @EventListener
    public void onApplicationEvent(final RateLimitEvent event) {
        auditService.processEvent(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEventTransactionFinish(final TransactionEvent event) {
        auditService.processEvent(event);
    }

}