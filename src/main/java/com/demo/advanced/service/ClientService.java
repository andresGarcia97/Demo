package com.demo.advanced.service;

import com.demo.advanced.domain.client.Client;
import com.demo.advanced.dto.ClientDTO;

import java.util.List;

public interface ClientService {

	ClientDTO save(ClientDTO client);

	ClientDTO update(ClientDTO client);

	List<ClientDTO> findAll();

	void delete(Long clientId);

	Client findById(Long clientId);

}
