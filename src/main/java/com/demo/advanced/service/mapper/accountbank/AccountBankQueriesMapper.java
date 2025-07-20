package com.demo.advanced.service.mapper.accountbank;

import com.demo.advanced.dto.response.AccountBankResponse;
import com.demo.advanced.entities.AccountBankEntity;
import com.demo.advanced.service.mapper.QueriesMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBankQueriesMapper extends QueriesMapper<AccountBankResponse, AccountBankEntity> {}
