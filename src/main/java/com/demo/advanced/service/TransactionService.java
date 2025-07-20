package com.demo.advanced.service;

import com.demo.advanced.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

	TransactionDTO saveAndFlush(TransactionDTO transactionDTO);

	List<TransactionDTO> findAll();

}
