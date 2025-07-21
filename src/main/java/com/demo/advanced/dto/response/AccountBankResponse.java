package com.demo.advanced.dto.response;

import com.demo.advanced.domain.enumeration.AccountState;
import com.demo.advanced.domain.enumeration.AccountType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record AccountBankResponse(Long id,
								  AccountType type,
								  Long number,
								  AccountState state,
								  BigDecimal balance,
								  ZonedDateTime creationDate,
								  ZonedDateTime modificationDate,
								  ClientResponse client) {

}
