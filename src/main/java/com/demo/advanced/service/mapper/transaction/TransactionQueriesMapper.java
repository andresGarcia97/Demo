package com.demo.advanced.service.mapper.transaction;

import com.demo.advanced.dto.response.TransactionResponse;
import com.demo.advanced.entities.TransactionEntity;
import com.demo.advanced.service.mapper.QueriesMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionQueriesMapper extends QueriesMapper<TransactionResponse, TransactionEntity> {}
