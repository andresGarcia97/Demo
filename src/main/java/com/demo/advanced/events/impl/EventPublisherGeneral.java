package com.demo.advanced.events.impl;

import com.demo.advanced.dto.event.RateLimitEvent;
import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.events.EventPublisher;
import com.demo.advanced.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherGeneral implements EventPublisher, ApplicationContextAware {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaProducer kafkaProducer;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publishRateLimitEvent(String key) {
        applicationContext.publishEvent(new RateLimitEvent(this, key));
    }

    @Override
    public void publishEventTransaction(final TransactionEvent transactionRequest) {
        applicationEventPublisher.publishEvent(transactionRequest);
    }

    @Override
    public void publishExternalTransaction(final TransactionEvent transactionRequest) {
        kafkaProducer.sendTransactionEvent(transactionRequest);
    }

}
