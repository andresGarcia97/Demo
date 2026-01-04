package com.demo.advanced.dto.event;

import org.apache.avro.reflect.AvroAlias;

@AvroAlias(space = "com.demo.advanced.dto.event", alias = "TransactionExternalEvent")
public record TransactionReceived(
        String type,
        Double amount,
        Long origin,
        Long destiny,
        String date) {
}