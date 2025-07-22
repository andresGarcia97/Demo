package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class EventListenerRateLimit implements ApplicationListener<RateLimitEvent> {

    @Override
    public void onApplicationEvent(final RateLimitEvent event) {
        final ZonedDateTime timeEvent = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());
        log.info("RateLimitEvent: {} at: {}", event, timeEvent);
    }
}
