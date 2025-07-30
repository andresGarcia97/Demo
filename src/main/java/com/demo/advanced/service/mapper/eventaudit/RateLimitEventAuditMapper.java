package com.demo.advanced.service.mapper.eventaudit;

import com.demo.advanced.entities.EventAuditEntity;
import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class RateLimitEventAuditMapper implements EntityMapper<RateLimitEvent, EventAuditEntity> {

    @Override
    public EventAuditEntity toEntity(final RateLimitEvent event) {
        final ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());
        return new EventAuditEntity(event.getKey(), date.toString(), String.format("Rate limit event for key: %s", event.getKey()));
    }

    @Override
    public RateLimitEvent toDomain(final EventAuditEntity entity) {
        return null;
    }

}