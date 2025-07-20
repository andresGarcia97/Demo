package com.demo.advanced.service;

import com.demo.advanced.domain.accountbank.AccountBank;
import com.demo.advanced.dto.AccountBankDTO;

import java.util.List;
import java.util.Optional;

public interface AccountBankService {

	AccountBankDTO save(AccountBankDTO accountBank);

	AccountBankDTO update(AccountBankDTO accountBank);

	List<AccountBankDTO> findAll();

	Optional<AccountBank> findAccountBank(AccountBankDTO account);

	void updateBalance(AccountBank account);

}
