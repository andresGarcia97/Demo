package com.demo.advanced.service.mapper.accountbank;


import com.demo.advanced.domain.accountbank.AccountBank;
import com.demo.advanced.dto.AccountBankDTO;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBankDomainMapper extends DomainMapper<AccountBankDTO, AccountBank> {}
