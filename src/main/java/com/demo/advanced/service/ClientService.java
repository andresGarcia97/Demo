package com.demo.advanced.service;

import com.demo.advanced.domain.client.Client;
import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.dto.response.ClientResponse;

import java.util.List;

public interface ClientService {

	ClientResponse save(ClientRequest client);

	ClientResponse update(ClientRequest client);

	List<ClientResponse> findAll();

	void delete(Long clientId);

	Client findById(Long clientId);

}
