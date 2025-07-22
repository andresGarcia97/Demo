package com.demo.advanced.controller;

import com.demo.advanced.controller.logging.Logging;
import com.demo.advanced.controller.ratelimit.RateLimited;
import com.demo.advanced.controller.ratelimit.RateLimitType;
import com.demo.advanced.dto.request.AccountBankRequest;
import com.demo.advanced.dto.response.AccountBankResponse;
import com.demo.advanced.service.AccountBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account-banks")
@RequiredArgsConstructor
public class AccountBankResource {

	private final AccountBankService accountBankService;

	@Logging
	@PostMapping("")
	public ResponseEntity<AccountBankResponse> createAccountBank(@RequestBody final AccountBankRequest accountBank) {
		return ResponseEntity.ok(accountBankService.save(accountBank));
	}

	@Logging
	@PutMapping("")
	public ResponseEntity<AccountBankResponse> updateAccountBank(@RequestBody final AccountBankRequest accountBank) {
		return ResponseEntity.ok().body(accountBankService.update(accountBank));
	}

	@Logging
	@RateLimited(RateLimitType.CLIENT)
	@GetMapping("/{clientId}")
	public List<AccountBankResponse> getAllByClientId(@PathVariable(name = "clientId") Long clientId) {
		return accountBankService.findAllByClientId(clientId);
	}

}
