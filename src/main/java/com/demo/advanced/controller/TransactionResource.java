package com.demo.advanced.controller;

import com.demo.advanced.controller.logging.Logging;
import com.demo.advanced.controller.ratelimit.RateLimited;
import com.demo.advanced.controller.ratelimit.RateLimitType;
import com.demo.advanced.dto.request.TransactionRequest;
import com.demo.advanced.dto.response.TransactionResponse;
import com.demo.advanced.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionResource {

	private final TransactionService transactionService;

	@Logging
	@PostMapping("")
	public ResponseEntity<TransactionResponse> createTransaction(@RequestBody final TransactionRequest transaction) {
		return ResponseEntity.ok(transactionService.saveAndFlush(transaction));
	}

	@RateLimited(RateLimitType.ACCOUNT)
	@GetMapping("/{accountId}")
	public List<TransactionResponse> getAllTransactions(@PathVariable Long accountId) {
		return transactionService.findAllByAccountId(accountId);
	}

}
