package com.demo.advanced.dto.response;

import com.demo.advanced.domain.enumeration.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record TransactionResponse(UUID id,
								  TransactionType type,
								  ZonedDateTime transactionDate,
								  BigDecimal amount,
								  @JsonInclude(Include.NON_NULL)
							      AccountBankResponse origin,
								  @JsonInclude(Include.NON_NULL)
							      AccountBankResponse destiny) {

}
