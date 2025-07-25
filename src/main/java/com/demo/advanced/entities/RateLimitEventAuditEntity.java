package com.demo.advanced.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RateLimitEventAuditEntity {

    private String key;
    private String eventDate;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("key")
    public String getKey() {
        return key;
    }

    @DynamoDbAttribute("eventDate")
    public String getEventDate() {
        return eventDate;
    }
}
