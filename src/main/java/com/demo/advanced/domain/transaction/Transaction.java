package com.demo.advanced.domain.transaction;

import com.demo.advanced.domain.accountbank.AccountBank;
import com.demo.advanced.entities.enumeration.TransactionType;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Data
public class Transaction {

	private UUID id;

	private TransactionType transactionType;

	@Setter
	private ZonedDateTime transactionDate;

	private BigDecimal amount;

	@Setter
	private AccountBank origin;

	@Setter
	private AccountBank destiny;

	public Transaction(final Transaction dataToCopy, TransactionType typeCopy) {
		super();
		this.transactionType = typeCopy;
		this.transactionDate = dataToCopy.getTransactionDate();
		this.amount = dataToCopy.getAmount();
		this.destiny = dataToCopy.getDestiny();
		this.origin = dataToCopy.getOrigin();
	}

	public Transaction validateCreation() {

		final TransactionType type = this.transactionType;
		this.validateObligatoryFields(type);

		this.setTransactionDate(ZonedDateTime.now());

		String accountObligatory = "origen y destino";

		final boolean destinyAccountExist = this.destiny != null;
		final boolean originAccountExist = this.origin != null;

		if(TransactionType.TRANSFERENCIA.equals(type) && destinyAccountExist && originAccountExist) {

			if(this.destiny.getId().equals(this.origin.getId())) {
				throw new TransactionException(new StringBuilder()
						.append("Una transaccion de tipo: ").append(type)
						.append(" el origen y el destino No pueden ser iguales")
						.append(" cuenta con ID: ").append(this.origin.getId())
						.append(" y numero: ").append(this.origin.getNumber())
						.toString()
				);
			}

			this.origin.substractAmountToBalanceAccount(this.amount);
			this.destiny.addAmountToBalanceAccount(this.amount);
			return this;
		}

		final boolean transactionIsConsignacion = TransactionType.CONSIGNACION.equals(type);
		if(transactionIsConsignacion && destinyAccountExist) {

			if(originAccountExist) {
				log.warn("validateCreation :: una {} no puede tener una cuenta de origen, borrando cuenta: {}",
						type, this.origin);				
				this.setOrigin(null);
			}
			this.destiny.addAmountToBalanceAccount(this.amount);
			return this;
		}
		else if(transactionIsConsignacion){
			accountObligatory = "destino";			
		}

		final boolean transactionIsRetiro = TransactionType.RETIRO.equals(type);
		if(transactionIsRetiro && originAccountExist) {

			if(destinyAccountExist) {
				log.warn("validateCreation :: un {} no puede tener una cuenta de destino, borrando cuenta: {}",
						type, this.destiny);				
				this.setDestiny(null);
			}
			this.origin.substractAmountToBalanceAccount(this.amount);
			return this;
		}
		else if(transactionIsRetiro){
			accountObligatory = "origen";			
		}

		log.error("validateCreation :: type: {}, accountObligatory: {}, destinyAccountExist: {}, originAccountExist: {}",
				type, accountObligatory, destinyAccountExist, originAccountExist);
		throw new TransactionException("Las transaciones de tipo: " + type.name() + " Deben contar con la cuenta de: " + accountObligatory);

	}

	private void validateObligatoryFields(final TransactionType type) {
		if(type == null) {
			throw new TransactionException("El tipo de transacción es obligatoria");
		}

		if(this.amount == null || BigDecimal.ZERO.compareTo(this.amount) >= 0) {
			throw new TransactionException("El monto de la transacción es obligatorio y debe ser mayor a Zero");
		}
	}

}
