package com.demo.advanced.dto.event;

import com.demo.advanced.domain.enumeration.TransactionType;
import com.demo.advanced.dto.request.TransactionRequest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TransactionEvent(TransactionType type,
                               BigDecimal amount,
                               Long origin,
                               Long destiny,
                               ZonedDateTime date) {

    public TransactionEvent(final TransactionRequest transactionSaved, ZonedDateTime transactionDate) {
        this(
                transactionSaved.type(),
                transactionSaved.amount(),
                transactionSaved.accountBankOrigin(),
                transactionSaved.accountBankDestiny(),
                transactionDate
        );
    }
}
