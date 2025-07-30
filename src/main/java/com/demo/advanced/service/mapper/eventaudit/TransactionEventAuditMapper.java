package com.demo.advanced.service.mapper.eventaudit;

import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.entities.AuditEntity;
import com.demo.advanced.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionEventAuditMapper implements EntityMapper<TransactionEvent, AuditEntity> {

    @Override
    public AuditEntity toEntity(final TransactionEvent event) {
        final LocalDateTime dateUntilSeconds = event.date().truncatedTo(ChronoUnit.SECONDS).toLocalDateTime();
        final String key = String.format("%s-%s-%s-%s", event.type(), event.origin(), event.destiny(), dateUntilSeconds);
        return new AuditEntity(key, event.date().toString(), String.format("Transaction event: %s with amount: %s", event.type(), event.amount()));
    }

    @Override
    public TransactionEvent toDomain(final AuditEntity entity) {
        return null;
    }

}