package com.demo.advanced.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventAuditEntity {

    private String key;
    private String eventDate;
    private String description;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("key")
    public String getKey() {
        return key;
    }

    @DynamoDbAttribute("eventDate")
    public String getEventDate() {
        return eventDate;
    }

    @DynamoDbAttribute("description")
    public String getDescription() {
        return description;
    }

}
