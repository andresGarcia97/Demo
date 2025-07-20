package com.demo.advanced.controller;

import com.demo.advanced.dto.TransactionDTO;
import com.demo.advanced.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionResource {

	private final TransactionService transactionService;

	@PostMapping("")
	public ResponseEntity<TransactionDTO> createTransaction(@RequestBody final TransactionDTO transaction) {
		log.info("REST request to generate transaction: {}", transaction);
		final TransactionDTO resultTransaction = transactionService.saveAndFlush(transaction);
		log.info("REST resultTransaction: {}", resultTransaction);
		return ResponseEntity.ok(resultTransaction);
	}

	@GetMapping("")
	public List<TransactionDTO> getAllTransactions() {
		return transactionService.findAll();
	}

}
