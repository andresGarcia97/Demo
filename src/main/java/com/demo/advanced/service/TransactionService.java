package com.demo.advanced.service;

import com.demo.advanced.dto.response.TransactionResponse;
import com.demo.advanced.dto.request.TransactionRequest;

import java.util.List;

public interface TransactionService {

	TransactionResponse createTransaction(TransactionRequest transactionResponse);

	List<TransactionResponse> findAllByAccountId(Long accountId);

}
