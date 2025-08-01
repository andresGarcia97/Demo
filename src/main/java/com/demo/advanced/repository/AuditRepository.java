package com.demo.advanced.repository;

import com.demo.advanced.entities.AuditEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuditRepository implements CommandLineRunner {

    private static final String RATE_LIMIT_EVENT_AUDIT_TABLE = "EventAudit";

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public void save(AuditEntity entity) {
        final DynamoDbTable<AuditEntity> table =
                dynamoDbEnhancedClient.table(RATE_LIMIT_EVENT_AUDIT_TABLE, TableSchema.fromBean(AuditEntity.class));
        table.putItem(entity);
    }

    @Override
    public void run(String... args) {

        try {
            dynamoDbClient.describeTable(table -> table.tableName(RATE_LIMIT_EVENT_AUDIT_TABLE));
            log.info("La tabla '{}' ya existe", RATE_LIMIT_EVENT_AUDIT_TABLE);
        }
        catch (ResourceNotFoundException e) {
            log.warn("La tabla '{}' no fue encontrada.", RATE_LIMIT_EVENT_AUDIT_TABLE);
            createTable();
        }

    }

    private void createTable() {

        final DynamoDbTable<AuditEntity> table =
                dynamoDbEnhancedClient.table(RATE_LIMIT_EVENT_AUDIT_TABLE, TableSchema.fromBean(AuditEntity.class));

        table.createTable();

        log.info("Esperando que la tabla '{}' este activa (timeout de 15s)", RATE_LIMIT_EVENT_AUDIT_TABLE);
        final DynamoDbWaiter waiter = dynamoDbClient.waiter();

        waiter.waitUntilTableExists(
                create -> create.tableName(RATE_LIMIT_EVENT_AUDIT_TABLE),
                timeout -> timeout.waitTimeout(Duration.ofSeconds(15))
        );

        log.info("La tabla '{}' fue creada y esta activa", RATE_LIMIT_EVENT_AUDIT_TABLE);
    }

}