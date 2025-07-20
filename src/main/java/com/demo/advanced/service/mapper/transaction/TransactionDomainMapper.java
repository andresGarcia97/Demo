package com.demo.advanced.service.mapper.transaction;

import com.demo.advanced.domain.transaction.Transaction;
import com.demo.advanced.dto.request.TransactionRequest;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDomainMapper extends DomainMapper<TransactionRequest, Transaction> {}
