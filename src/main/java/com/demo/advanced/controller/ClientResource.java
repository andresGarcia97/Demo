package com.demo.advanced.controller;

import com.demo.advanced.controller.logging.Logging;
import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.dto.response.ClientResponse;
import com.demo.advanced.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientResource {

	private final ClientService clientService;

	@Logging
	@PostMapping("")
	public ResponseEntity<ClientResponse> create(@RequestBody final ClientRequest client) {
		return ResponseEntity.ok(clientService.save(client));
	}

	@Logging
	@PutMapping("")
	public ResponseEntity<ClientResponse> update(@RequestBody final ClientRequest client) {
		return ResponseEntity.ok(clientService.update(client));
	}

	@Logging
	@DeleteMapping("/{clientId}")
	public ResponseEntity<Void> delete(@PathVariable Long clientId) {
		clientService.delete(clientId);
		return ResponseEntity.noContent().build();
	}
}
