package com.demo.advanced.dto;

import com.demo.advanced.entities.enumeration.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record TransactionDTO(UUID id,
							 TransactionType type,
							 ZonedDateTime transactionDate,
							 BigDecimal amount,
							 @JsonInclude(Include.NON_NULL)
							 AccountBankDTO origin,
							 @JsonInclude(Include.NON_NULL)
							 AccountBankDTO destiny) {

}
