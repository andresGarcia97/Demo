package com.demo.advanced.controller;

import com.demo.advanced.dto.response.AccountBankResponse;
import com.demo.advanced.dto.request.AccountBankRequest;
import com.demo.advanced.service.AccountBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/account-banks")
@RequiredArgsConstructor
public class AccountBankResource {

	private final AccountBankService accountBankService;

	@PostMapping("")
	public ResponseEntity<AccountBankResponse> createAccountBank(@RequestBody final AccountBankRequest accountBank) {
		log.info("REST request to save accountBank: {}", accountBank);
		final AccountBankResponse saved = accountBankService.save(accountBank);
		log.info("REST result saved: {}", saved);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("")
	public ResponseEntity<AccountBankResponse> updateAccountBank(@RequestBody final AccountBankRequest accountBank) {
		log.info("REST request to update accountBank: {}", accountBank);
		final AccountBankResponse updated = accountBankService.update(accountBank);
		log.info("REST result updated: {}", updated);
		return ResponseEntity.ok().body(updated);
	}

	@GetMapping("/{clientId}")
	public List<AccountBankResponse> getAllByClientId(@PathVariable(name = "clientId") Long clientId) {
		return accountBankService.findAllByClientId(clientId);
	}

}
