package com.demo.advanced.dto.request;

import com.demo.advanced.entities.enumeration.TransactionType;

import java.math.BigDecimal;

public record TransactionRequest(TransactionType type,
                                 BigDecimal amount,
								 Long accountBankOrigin,
                                 Long accountBankDestiny) {

}
