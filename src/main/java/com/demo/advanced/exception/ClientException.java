package com.demo.advanced.exception;

public final class ClientException extends RuntimeException {

	public static final String CLIENT_NOT_EXIST = "No existe un cliente con el ID proporcionado";

	public ClientException(final String msgError) {
		super(msgError);
	}

}
