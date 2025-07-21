package com.demo.advanced.service.impl;


import com.demo.advanced.domain.AccountBank;
import com.demo.advanced.exception.AccountBankException;
import com.demo.advanced.dto.response.AccountBankResponse;
import com.demo.advanced.dto.request.AccountBankRequest;
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
	public AccountBankResponse save(final AccountBankRequest accountBank)  {

		final Long clientId = Optional.ofNullable(accountBank.clientId())
				.orElseThrow(() -> new AccountBankException("La cuenta debe estar asociada a un cliente obligatoriamente"));

		final AccountBank toValidate = domainMapper.toDomain(accountBank).validateCreation();
		toValidate.setClient(clientService.findById(clientId));

		accountBankRepository.findByNumber(toValidate.getNumber())
		.ifPresent(accountNumberRepeated -> {
			toValidate.setNumber(toValidate.generateRandomNumberAccount());
			log.warn("save :: Numero de cuenta repetido, generando de nuevo, newGeneration: {}, previousExist: {}, accountId: {}",
					toValidate.getNumber(), accountNumberRepeated.getNumber(), accountNumberRepeated.getId());
		});

		final AccountBankEntity saved = accountBankRepository.save(entityMapper.toEntity(toValidate));

		return queriesMapper.toDto(saved);
	}

	@Override
	public AccountBankResponse update(final AccountBankRequest accountBank) {

		final AccountBankEntity accountBankFound = accountBankRepository.findById(accountBank.id())
				.orElseThrow(() -> new AccountBankException("No existe una cuenta bancaria con el ID proporcionado"));

		final AccountBank toUpdate = domainMapper.toDomain(accountBank).validateUpdate(entityMapper.toDomain(accountBankFound));
		final AccountBankEntity updated = accountBankRepository.save(entityMapper.toEntity(toUpdate));

		return queriesMapper.toDto(updated);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AccountBankResponse> findAllByClientId(Long clientId) {
		return queriesMapper.toDtoList(accountBankRepository.findAllByClientId(clientId));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AccountBank> findById(final Long accountId) {
		return accountId == null
				? Optional.empty()
				: accountBankRepository.findById(accountId).map(entityMapper::toDomain);
	}

	@Override
	public void updateBalance(final AccountBank account) {
		accountBankRepository.updateBalanceById(account.getBalance(), account.getId());
		accountBankRepository.flush();
	}

}
