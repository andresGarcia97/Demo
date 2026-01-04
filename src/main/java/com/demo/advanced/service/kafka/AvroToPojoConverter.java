package com.demo.advanced.service.kafka;

import com.demo.advanced.dto.event.TransactionReceived;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class AvroToPojoConverter extends MessagingMessageConverter {

    @Override
    public Object extractAndConvertValue(ConsumerRecord<?, ?> consumerRecord, Type type) {

        final Object value = consumerRecord.value();

        if (value instanceof GenericRecord genericRecord && type.getTypeName().equals(TransactionReceived.class.getName())) {

            return new TransactionReceived(
                    genericRecord.get("type").toString(),
                    (Double) genericRecord.get("amount"),
                    (Long) genericRecord.get("origin"),
                    (Long) genericRecord.get("destiny"),
                    genericRecord.get("date") != null ? genericRecord.get("date").toString() : null
            );
        }

        return super.extractAndConvertValue(consumerRecord, type);
    }
}