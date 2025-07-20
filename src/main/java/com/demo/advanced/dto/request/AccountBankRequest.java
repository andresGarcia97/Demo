package com.demo.advanced.dto.request;

import com.demo.advanced.entities.enumeration.AccountState;
import com.demo.advanced.entities.enumeration.AccountType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record AccountBankRequest(Long id,
                                 AccountType type,
                                 Long number,
                                 AccountState state,
                                 BigDecimal balance,
                                 ZonedDateTime creationDate,
                                 ZonedDateTime modificationDate,
                                 Long clientId) {

}
