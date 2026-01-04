package com.demo.advanced.events;

import com.demo.advanced.dto.event.TransactionEvent;

public interface EventPublisher {

    void publishRateLimitEvent(String key);

    void publishEventTransaction(TransactionEvent transactionRequest);

    void publishExternalTransaction(TransactionEvent transactionRequest);
}
