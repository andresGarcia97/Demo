package com.demo.advanced.service.impl;


import com.demo.advanced.domain.accountbank.AccountBank;
import com.demo.advanced.domain.accountbank.AccountBankException;
import com.demo.advanced.dto.AccountBankDTO;
import com.demo.advanced.dto.ClientDTO;
import com.demo.advanced.entities.AccountBankEntity;
import com.demo.advanced.repository.AccountBankRepository;
import com.demo.advanced.service.AccountBankService;
import com.demo.advanced.service.ClientService;
import com.demo.advanced.service.mapper.accountbank.AccountBankDomainMapper;
import com.demo.advanced.service.mapper.accountbank.AccountBankEntityMapper;
import com.demo.advanced.service.mapper.accountbank.AccountBankQueriesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountBankServiceImpl implements AccountBankService {

	private final AccountBankRepository accountBankRepository;
	private final ClientService clientService;

	private final AccountBankQueriesMapper queriesMapper;
	private final AccountBankDomainMapper domainMapper;
	private final AccountBankEntityMapper entityMapper;

	@Override
	public AccountBankDTO save(final AccountBankDTO accountBank)  {

		final ClientDTO client = accountBank.client();
		if(client == null || client.id() == null) {
			throw new AccountBankException("La cuenta debe estar asociada a un cliente obligatoriamente");
		}

		final AccountBank toValidate = domainMapper.toDomain(accountBank).validateCreation();
		toValidate.setClient(clientService.findById(client.id()));

		accountBankRepository.findByNumber(toValidate.getNumber())
		.ifPresent(accountNumberRepeated -> {
			toValidate.setNumber(toValidate.generateRandonNumberAccount());
			log.warn("save :: Numero de cuenta repetido, generando de nuevo, newGeneration: {}, previousExist: {}, accountId: {}",
					toValidate.getNumber(), accountNumberRepeated.getNumber(), accountNumberRepeated.getId());
		});

		final AccountBankEntity saved = accountBankRepository.save(entityMapper.toEntity(toValidate));

		return queriesMapper.toDto(saved);
	}

	@Override
	public AccountBankDTO update(final AccountBankDTO accountBank) throws AccountBankException {

		final AccountBankEntity accountBankFound = accountBankRepository.findById(accountBank.id())
				.orElseThrow(() -> new AccountBankException("No existe una cuenta bancaria con el ID proporcionado"));

		final AccountBank toUpdate = domainMapper.toDomain(accountBank).validateUpdate(entityMapper.toDomain(accountBankFound));
		final AccountBankEntity updated = accountBankRepository.save(entityMapper.toEntity(toUpdate));

		return queriesMapper.toDto(updated);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AccountBankDTO> findAll() {
		return accountBankRepository.findAll().stream().map(queriesMapper::toDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AccountBank> findAccountBank(final AccountBankDTO account) {
		return (account == null || account.id() == null)
				? Optional.empty()
				: accountBankRepository.findById(account.id()).map(entityMapper::toDomain);
	}

	@Override
	public void updateBalance(final AccountBank account) {
		accountBankRepository.updateBalanceById(account.getBalance(), account.getId());
		accountBankRepository.flush();
	}

}
