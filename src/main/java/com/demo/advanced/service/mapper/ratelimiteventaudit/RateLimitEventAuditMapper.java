package com.demo.advanced.service.mapper.ratelimiteventaudit;

import com.demo.advanced.entities.RateLimitEventAuditEntity;
import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class RateLimitEventAuditMapper implements EntityMapper<RateLimitEvent, RateLimitEventAuditEntity> {

    @Override
    public RateLimitEventAuditEntity toEntity(final RateLimitEvent event) {
        final ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());
        return new RateLimitEventAuditEntity(event.getKey(), date.toString());
    }

    @Override
    public RateLimitEvent toDomain(final RateLimitEventAuditEntity entity) {
        return null;
    }

}