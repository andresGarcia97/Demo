package com.demo.advanced.service.mapper.accountbank;

import com.demo.advanced.domain.AccountBank;
import com.demo.advanced.entities.AccountBankEntity;
import com.demo.advanced.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBankEntityMapper extends EntityMapper<AccountBank, AccountBankEntity> {}
