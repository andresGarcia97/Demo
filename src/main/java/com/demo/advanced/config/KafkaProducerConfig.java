package com.demo.advanced.config;

import com.demo.advanced.dto.event.TransactionExternalEvent;
import io.apicurio.registry.resolver.config.SchemaResolverConfig;
import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${apicurio.registry.url}")
    private String registryUrl;

    @Bean
    public ProducerFactory<String, TransactionExternalEvent> producerFactory() {

        final Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class);

        // Configuración Apicurio
        props.put(SchemaResolverConfig.REGISTRY_URL, registryUrl);
        props.put(SchemaResolverConfig.AUTO_REGISTER_ARTIFACT, Boolean.TRUE);
        props.put(SchemaResolverConfig.AUTO_REGISTER_ARTIFACT_IF_EXISTS, "CREATE_VERSION");
        props.put(SchemaResolverConfig.ARTIFACT_RESOLVER_STRATEGY, "io.apicurio.registry.serde.strategy.TopicIdStrategy");
        props.put("apicurio.registry.serde.as-confluent", "true");

        // Evitar duplicados
        props.put("apicurio.registry.serde.check-registry-state", "true");
        props.put("apicurio.registry.serde.avro-encoding", "BINARY");

        // Asignacion de esquema especifico
        props.put(SchemaResolverConfig.EXPLICIT_ARTIFACT_GROUP_ID, "transactions");
        props.put(SchemaResolverConfig.EXPLICIT_ARTIFACT_ID, "transactions-events");

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, TransactionExternalEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}