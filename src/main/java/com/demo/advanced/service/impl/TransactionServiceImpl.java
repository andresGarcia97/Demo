package com.demo.advanced.service.impl;

import com.demo.advanced.domain.AccountBank;
import com.demo.advanced.exception.AccountBankException;
import com.demo.advanced.domain.Transaction;
import com.demo.advanced.exception.TransactionException;
import com.demo.advanced.dto.response.TransactionResponse;
import com.demo.advanced.dto.request.TransactionRequest;
import com.demo.advanced.entities.TransactionEntity;
import com.demo.advanced.domain.enumeration.TransactionType;
import com.demo.advanced.repository.TransactionRepository;
import com.demo.advanced.service.AccountBankService;
import com.demo.advanced.service.TransactionService;
import com.demo.advanced.service.mapper.transaction.TransactionDomainMapper;
import com.demo.advanced.service.mapper.transaction.TransactionEntityMapper;
import com.demo.advanced.service.mapper.transaction.TransactionQueriesMapper;
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
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	private final AccountBankService accountBankService;

	private final TransactionQueriesMapper queriesMapper;
	private final TransactionDomainMapper domainMapper;
	private final TransactionEntityMapper entityMapper;

	@Override
	public TransactionResponse saveAndFlush(final TransactionRequest transaction) {

		final Optional<AccountBank> existAccountOrigin = accountBankService.findById(transaction.accountBankOrigin());
		final Optional<AccountBank> existAccountDestiny = accountBankService.findById(transaction.accountBankDestiny());

		if(existAccountOrigin.isEmpty() && existAccountDestiny.isEmpty()) {
			throw new TransactionException("Se debe referenciar al menos una cuenta, ya sea destino o origen");
		}

		final Transaction toValidate = domainMapper.toDomain(transaction);

		existAccountOrigin.ifPresent(toValidate::setOrigin);
		existAccountDestiny.ifPresent(toValidate::setDestiny);

		toValidate.validateCreation();

		final AccountBank destiny = toValidate.getDestiny();
		final AccountBank origin = toValidate.getOrigin();

		if(destiny != null && TransactionType.CONSIGNACION.equals(toValidate.getType())) {
			accountBankService.updateBalance(destiny);
		}

		if(origin != null && TransactionType.RETIRO.equals(toValidate.getType())) {
			accountBankService.updateBalance(origin);
		}

		if(destiny != null && origin != null && TransactionType.TRANSFERENCIA.equals(toValidate.getType())) {

			log.info("toValidate {}", toValidate);

			accountBankService.updateBalance(destiny);
			accountBankService.updateBalance(origin);

			final Transaction copyConsignacion = new Transaction(toValidate, TransactionType.CONSIGNACION);
			final TransactionEntity savedConsignacion = transactionRepository.saveAndFlush(entityMapper.toEntity(copyConsignacion));

			final Transaction copyRetiro = new Transaction(toValidate, TransactionType.RETIRO);  
			final TransactionEntity savedRetiro = transactionRepository.saveAndFlush(entityMapper.toEntity(copyRetiro));
			log.info("saveAndFlush :: consignacionId: {}, retiroId: {}", savedConsignacion.getId(), savedRetiro.getId());
		}

		final TransactionEntity saved = transactionRepository.saveAndFlush(entityMapper.toEntity(toValidate));

		return queriesMapper.toDto(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponse> findAllByAccountId(Long accountId) {

		final var account = accountBankService.findById(accountId)
				.orElseThrow(() -> new AccountBankException(AccountBankException.ACCOUNT_NOT_EXIST));

		log.info("findAllByAccountId accountId: {}, accountNumber: {}", accountId, account.getNumber());

		return transactionRepository.findByOrigin_IdOrDestiny_Id(accountId, accountId).stream()
				.map(queriesMapper::toDto)
				.toList();
	}

}
