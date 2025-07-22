package com.demo.advanced.service;

import com.demo.advanced.domain.Client;
import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.dto.response.ClientResponse;

public interface ClientService {

	ClientResponse save(ClientRequest client);

	ClientResponse update(ClientRequest client);

	void delete(Long clientId);

	Client findById(Long clientId);

}
