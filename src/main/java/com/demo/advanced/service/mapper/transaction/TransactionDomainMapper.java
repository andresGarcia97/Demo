package com.demo.advanced.service.mapper.transaction;

import com.demo.advanced.domain.transaction.Transaction;
import com.demo.advanced.dto.TransactionDTO;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDomainMapper extends DomainMapper<TransactionDTO, Transaction> {}
