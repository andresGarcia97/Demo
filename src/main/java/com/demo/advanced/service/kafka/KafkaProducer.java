package com.demo.advanced.service.kafka;

import com.demo.advanced.dto.event.TransactionEvent;
import com.demo.advanced.dto.event.TransactionExternalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, TransactionExternalEvent> kafkaTemplate;

    @Value("${spring.kafka.producer.topics.transactions}")
    private String transactionsTopic;

    public void sendTransactionEvent(final TransactionEvent transactionEvent) {

        try {

            final var externalEvent = buildEvent(transactionEvent);

            kafkaTemplate.send(externalEvent)
                    .orTimeout(10, TimeUnit.SECONDS)
                    .thenAccept(result -> log.info("offset={} << {}", result.getRecordMetadata().offset(), externalEvent.value()))
                    .exceptionally(ex -> {
                        log.error("Error publishing externalEvent={} with ErrorMsg: {}", externalEvent, ex.getMessage(), ex);
                        return null;
                    });

        }
        catch (Exception e) {
            log.error("Error publishing transactionEvent={}", transactionEvent, e);
        }
    }

    private ProducerRecord<String, TransactionExternalEvent> buildEvent(final TransactionEvent transactionEvent) {

        final String transactionType = transactionEvent.type().name();
        final String keyDate = transactionEvent.date()
                .toLocalDateTime()
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final String key = transactionType + "-" + keyDate;

        final TransactionExternalEvent externalEvent = TransactionExternalEvent.newBuilder()
                .setType(transactionType)
                .setAmount(transactionEvent.amount().doubleValue())
                .setOrigin(transactionEvent.origin())
                .setDestiny(transactionEvent.destiny())
                .setDate(transactionEvent.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .build();

        return new ProducerRecord<>(transactionsTopic, key, externalEvent);
    }

}
