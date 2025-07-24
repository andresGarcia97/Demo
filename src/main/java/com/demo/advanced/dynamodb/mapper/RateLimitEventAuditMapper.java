package com.demo.advanced.dynamodb.mapper;

import com.demo.advanced.dynamodb.entities.RateLimitEventAuditEntity;
import com.demo.advanced.dto.event.RateLimitEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class RateLimitEventAuditMapper {

    public RateLimitEventAuditEntity toEntity(RateLimitEvent event) {
        RateLimitEventAuditEntity entity = new RateLimitEventAuditEntity();
        entity.setKey(event.getKey());
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());
        entity.setEventDate(date.toString());
        return entity;
    }
}