package com.demo.advanced.controller;

import com.demo.advanced.dto.AccountBankDTO;
import com.demo.advanced.service.AccountBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<AccountBankDTO> createAccountBank(@RequestBody final AccountBankDTO accountBank) {
		log.info("REST request to save accountBank: {}", accountBank);
		final AccountBankDTO saved = accountBankService.save(accountBank);
		log.info("REST result saved: {}", saved);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("")
	public ResponseEntity<AccountBankDTO> updateAccountBank(@RequestBody final AccountBankDTO accountBank) {
		log.info("REST request to update accountBank: {}", accountBank);
		final AccountBankDTO updated = accountBankService.update(accountBank);
		log.info("REST result updated: {}", updated);
		return ResponseEntity.ok().body(updated);
	}

	@GetMapping("")
	public List<AccountBankDTO> getAll() {
		return accountBankService.findAll();
	}

}
