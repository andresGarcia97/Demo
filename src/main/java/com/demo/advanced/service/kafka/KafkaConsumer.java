package com.demo.advanced.service.kafka;

import com.demo.advanced.dto.event.TransactionReceived;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(
            topics = "${spring.kafka.producer.topics.transactions}",
            containerFactory = "kafkaListenerContainerFactoryTransactionReceived"
    )
    public void handle(@Payload final TransactionReceived message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("partition={}, offset={} >> {}", partition, offset, message);
    }

}
