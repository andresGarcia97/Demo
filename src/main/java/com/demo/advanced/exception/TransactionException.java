package com.demo.advanced.exception;

public final class TransactionException extends RuntimeException {
	
	public TransactionException(final String msgError) {
		super(msgError);
	}

}
