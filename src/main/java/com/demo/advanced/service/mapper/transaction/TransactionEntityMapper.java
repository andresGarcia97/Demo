package com.demo.advanced.service.mapper.transaction;

import com.demo.advanced.domain.transaction.Transaction;
import com.demo.advanced.entities.TransactionEntity;
import com.demo.advanced.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper extends EntityMapper<Transaction, TransactionEntity> {}
