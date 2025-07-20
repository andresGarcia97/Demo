package com.demo.advanced.domain.transaction;

public final class TransactionException extends RuntimeException {
	
	public TransactionException(final String msgError) {
		super(msgError);
	}

}
