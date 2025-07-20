package com.demo.advanced.service;

import com.demo.advanced.domain.accountbank.AccountBank;
import com.demo.advanced.dto.response.AccountBankResponse;
import com.demo.advanced.dto.request.AccountBankRequest;

import java.util.List;
import java.util.Optional;

public interface AccountBankService {

	AccountBankResponse save(AccountBankRequest accountBank);

	AccountBankResponse update(AccountBankRequest accountBank);

	List<AccountBankResponse> findAll();

	Optional<AccountBank> findById(Long accountId);

	void updateBalance(AccountBank account);

}
