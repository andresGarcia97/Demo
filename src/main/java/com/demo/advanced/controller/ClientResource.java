package com.demo.advanced.controller;

import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.dto.response.ClientResponse;
import com.demo.advanced.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientResource {

	private final ClientService clientService;

	@PostMapping("")
	public ResponseEntity<ClientResponse> create(@RequestBody final ClientRequest client) {
		log.info("REST request to save client: {}", client);
		final ClientResponse saved = clientService.save(client);
		log.info("REST result saved : {}", saved);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("")
	public ResponseEntity<ClientResponse> update(@RequestBody final ClientRequest client) {
		log.info("REST request to update client: {}", client);
		final ClientResponse update = clientService.update(client);
		log.info("REST result update : {}", update);
		return ResponseEntity.ok(update);
	}

	@GetMapping("")
	public List<ClientResponse> getAll() {
		return clientService.findAll();
	}

	@DeleteMapping("/{clientId}")
	public ResponseEntity<Void> delete(@PathVariable(name = "clientId") Long clientId) {
		log.info("REST request to delete client, clientId: {}", clientId);
		clientService.delete(clientId);
		return ResponseEntity.noContent().build();
	}
}
