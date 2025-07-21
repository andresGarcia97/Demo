package com.demo.advanced.service.mapper.accountbank;

import com.demo.advanced.domain.AccountBank;
import com.demo.advanced.dto.request.AccountBankRequest;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBankDomainMapper extends DomainMapper<AccountBankRequest, AccountBank> {}
