package com.demo.advanced.config;

import com.demo.advanced.service.kafka.AvroToPojoConverter;
import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.apicurio.registry.serde.avro.ReflectAvroDatumProvider;
import io.apicurio.registry.serde.config.SerdeConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${apicurio.registry.url}")
    private String registryUrl;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroup;

    @Value("${spring.kafka.consumer.topics.retry.dltTopicSuffix}")
    private String dltTopicSuffix;

    private final AvroToPojoConverter avroToPojoConverter;

    // 1. CONSUMER FACTORY (Lectura)
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {

        final Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumerProps.put(SerdeConfig.REGISTRY_URL, registryUrl);
        consumerProps.put("apicurio.registry.serde.as-confluent", "true");
        consumerProps.put("apicurio.registry.avro.datum-provider", ReflectAvroDatumProvider.class.getName());
        consumerProps.put("apicurio.registry.serde.avro-encoding", "BINARY");

        final AvroKafkaDeserializer<Object> avroDeserializer = new AvroKafkaDeserializer<>();
        avroDeserializer.configure(consumerProps, false);

        final ErrorHandlingDeserializer<Object> errorDeserializer = new ErrorHandlingDeserializer<>(avroDeserializer);

        return new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                errorDeserializer
        );
    }

    // 2. DLT PRODUCER FACTORY (Tipado Estricto: String, byte[])
    @Bean
    public ProducerFactory<String, byte[]> binaryProducerFactory() {

        final Map<String, Object> props = Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                new ByteArraySerializer()
        );
    }

    @Bean(name = "binaryKafkaTemplate")
    public KafkaTemplate<String, byte[]> binaryKafkaTemplate() {
        return new KafkaTemplate<>(binaryProducerFactory());
    }

    // 3. CONTAINER FACTORY
    @Bean(name = "kafkaListenerContainerFactoryTransactionReceived")
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            @Qualifier("binaryKafkaTemplate") KafkaTemplate<String, byte[]> dltTemplate) {

        final ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        factory.setRecordMessageConverter(avroToPojoConverter);

        // Recoverer
        final DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                dltTemplate,
                (r, e) -> new TopicPartition(r.topic() + dltTopicSuffix, r.partition())
        );

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0));
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}