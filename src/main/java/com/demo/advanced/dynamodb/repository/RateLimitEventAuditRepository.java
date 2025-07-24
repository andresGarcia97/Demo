package com.demo.advanced.dynamodb.repository;

import com.demo.advanced.dynamodb.entities.RateLimitEventAuditEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class RateLimitEventAuditRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public void save(RateLimitEventAuditEntity entity) {
        DynamoDbTable<RateLimitEventAuditEntity> table =
                dynamoDbEnhancedClient.table("RateLimitEventAudit", TableSchema.fromBean(RateLimitEventAuditEntity.class));
        table.putItem(entity);
    }
}