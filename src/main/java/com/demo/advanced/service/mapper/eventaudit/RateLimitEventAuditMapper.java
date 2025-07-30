package com.demo.advanced.service.mapper.eventaudit;

import com.demo.advanced.entities.AuditEntity;
import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class RateLimitEventAuditMapper implements EntityMapper<RateLimitEvent, AuditEntity> {

    @Override
    public AuditEntity toEntity(final RateLimitEvent event) {
        final ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());
        return new AuditEntity(event.getKey(), date.toString(), String.format("Rate limit event for key: %s", event.getKey()));
    }

    @Override
    public RateLimitEvent toDomain(final AuditEntity entity) {
        return null;
    }

}