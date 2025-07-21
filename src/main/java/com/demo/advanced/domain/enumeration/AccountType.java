package com.demo.advanced.domain.enumeration;

import lombok.Getter;

@Getter
public enum AccountType {

	CUENTA_CORRIENTE(33),
	CUENTA_AHORROS(53);

	private final int startNumber;

	AccountType(int startNumber) {
		this.startNumber = startNumber;
	}

}
