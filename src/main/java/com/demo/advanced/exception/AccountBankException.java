package com.demo.advanced.exception;

public final class AccountBankException extends RuntimeException {

	public static final String ACCOUNT_NOT_EXIST = "No existe una cuenta con el ID proporcionado";

	public AccountBankException(final String msgError) {
		super(msgError);
	}

}
