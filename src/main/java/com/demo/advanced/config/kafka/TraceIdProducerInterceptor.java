package com.demo.advanced.config.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TraceIdProducerInterceptor implements ProducerInterceptor<String, Object> {

    public static final String TRACE_ID_KEY = "trace-id";

    @Override
    public ProducerRecord<String, Object> onSend(final ProducerRecord<String, Object> producerRecord) {

        final String traceId = MDC.get(TRACE_ID_KEY);

        if (traceId != null && !traceId.isBlank()) {
            producerRecord.headers().add(TRACE_ID_KEY, traceId.getBytes(StandardCharsets.UTF_8));
        }

        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // No necesitamos hacer nada aquí para el trace-id
    }

    @Override
    public void close() {
        // No hay recursos que cerrar
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Configuración adicional si fuera necesaria
    }
}