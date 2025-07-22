package com.demo.advanced.service.impl;

import com.demo.advanced.domain.Client;
import com.demo.advanced.exception.ClientException;
import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.dto.response.ClientResponse;
import com.demo.advanced.entities.AccountBankEntity;
import com.demo.advanced.entities.ClientEntity;
import com.demo.advanced.repository.ClientRepository;
import com.demo.advanced.service.ClientService;
import com.demo.advanced.service.mapper.client.ClientDomainMapper;
import com.demo.advanced.service.mapper.client.ClientEntityMapper;
import com.demo.advanced.service.mapper.client.ClientQueriesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	private final ClientQueriesMapper queriesMapper;
	private final ClientDomainMapper domainMapper;
	private final ClientEntityMapper entityMapper;

	@Override
	public ClientResponse save(final ClientRequest client) {

		final Client toValidate = domainMapper.toDomain(client).validateCreation();
		final ClientEntity saved = clientRepository.save(entityMapper.toEntity(toValidate));

		return queriesMapper.toDto(saved);
	}

	@Override
	public ClientResponse update(final ClientRequest client) {

		final ClientEntity clientFound = clientRepository.findById(client.id())
				.orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXIST));

		final Client toUpdate = domainMapper.toDomain(client).validateUpdate(entityMapper.toDomain(clientFound));
		final ClientEntity updated = clientRepository.save(entityMapper.toEntity(toUpdate));

		return queriesMapper.toDto(updated);
	}

	@Override
	public void delete(Long clientId) throws ClientException {

		final Optional<ClientEntity> clientExist = clientRepository.findById(clientId);

		if(clientExist.isEmpty()) {
			return;
		}

		final ClientEntity clientToDelete = clientExist.get();

		if(clientToDelete.getAccounts() != null && !clientToDelete.getAccounts().isEmpty()) {

			final List<Long> accountIds = clientToDelete.getAccounts().stream().map(AccountBankEntity::getNumber).toList();
			throw new ClientException("No se puede eliminar este cliente, ya que esta asociado a las siguientes cuentas: " + accountIds);
		}

		clientRepository.deleteById(clientId);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long clientId) {
		return clientRepository.findById(clientId)
				.map(entityMapper::toDomain)
				.orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXIST));
	}

}
