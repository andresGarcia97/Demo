package com.demo.advanced.domain.accountbank;

import com.demo.advanced.domain.client.Client;
import com.demo.advanced.entities.enumeration.AccountState;
import com.demo.advanced.entities.enumeration.AccountType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Random;

@Slf4j
@Data
public class AccountBank {

	private final Random random = new Random();

	private Long id;

	private AccountType type;

	private Long number;

	private AccountState state;

	private BigDecimal balance;

	private ZonedDateTime creationDate;

	private ZonedDateTime modificationDate;

	private Client client;

	public AccountBank validateCreation() {

		final AccountType accountType = this.type;
		if(accountType == null) {
			throw new AccountBankException("El tipo de cuenta es obligatorio");
		}

		final boolean accountIsAhorros = AccountType.CUENTA_AHORROS.equals(accountType);

		if(accountIsAhorros) {
			this.setState(AccountState.ACTIVA);
		}
		else if(this.state == null) {
			throw new AccountBankException("El estado de la cuenta es obligatorio, al ser una cuenta de tipo: " + accountType);
		}

		if(this.balance == null || BigDecimal.ZERO.compareTo(this.balance) >= 0) {
			log.warn("validateCreation :: no tiene balance o es menor a zero: {}, modificando a Zero", this.balance);
			this.setBalance(BigDecimal.ZERO);
		}

		if(this.modificationDate != null) {
			log.warn("validateCreation :: intento de creacion con fecha de modificacion: {}, borrando campo", this.modificationDate);
			this.setModificationDate(null);
		}

		this.setNumber(generateRandomNumberAccount());
		this.setCreationDate(ZonedDateTime.now().withNano(0));
		return this;
	}

	public Long generateRandomNumberAccount() {
		final int random8Numbers = 10000000 + random.nextInt(99999999);
		return Long.valueOf(this.type.getStartNumber() + String.valueOf(random8Numbers));
	}

	public AccountBank validateUpdate(final AccountBank accountBank) {

		final AccountState stateToUpdate = this.state;
		if(stateToUpdate == null) {
			throw new AccountBankException("El estado de la cuenta, es obligatorio para su actualización");
		}

		if(stateToUpdate.equals(accountBank.getState())) {
			return accountBank;
		}

		if(AccountState.CANCELADA.equals(stateToUpdate) && BigDecimal.ZERO.compareTo(accountBank.getBalance()) != 0) {
			throw new AccountBankException("Solo se pueden cancelar las cuentas con saldo 0, saldo actual: $" + accountBank.getBalance());
		}

		accountBank.setState(stateToUpdate);
		accountBank.setModificationDate(ZonedDateTime.now());
		return accountBank;
	}

	public void substractAmountToBalanceAccount(final BigDecimal amount) {

		final BigDecimal subtractResult = this.getBalance().subtract(amount);

		if(BigDecimal.ZERO.compareTo(subtractResult) > 0) {
			throw new AccountBankException(new StringBuilder()
					.append("No se puede restar la cantidad de: ").append(amount)
					.append(" ya que el saldo actual es: $").append(this.balance)
					.append(" y el saldo quedaria negativo: ").append(subtractResult)
					.append(" en la cuenta con ID: ").append(this.id)
					.append(" y numero: ").append(this.number)
					.toString());
		}

		this.setBalance(subtractResult);
	}

	public void addAmountToBalanceAccount(final BigDecimal amount) {
		this.setBalance(this.getBalance().add(amount));
	}

}
