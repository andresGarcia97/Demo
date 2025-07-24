package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.events.EventPublisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherRateLimit implements EventPublisher, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publishRateLimitEvent(String key) {
        applicationContext.publishEvent(new RateLimitEvent(this, key));
    }

}
