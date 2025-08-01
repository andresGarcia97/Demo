package com.demo.advanced.domain;

import com.demo.advanced.domain.enumeration.TransactionType;
import com.demo.advanced.exception.TransactionException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Data
@NoArgsConstructor
public class Transaction {

	private UUID id;

	private TransactionType type;

	@Setter
	private ZonedDateTime transactionDate;

	private BigDecimal amount;

	@Setter
	private AccountBank origin;

	@Setter
	private AccountBank destiny;

	public Transaction(final Transaction dataToCopy, TransactionType type) {
		super();
		this.type = type;
		this.transactionDate = dataToCopy.getTransactionDate();
		this.amount = dataToCopy.getAmount();
		this.destiny = dataToCopy.getDestiny();
		this.origin = dataToCopy.getOrigin();
	}

	public Transaction validateCreation() {

		final TransactionType transactionType = this.type;
		this.validateObligatoryFields(transactionType);

		this.setTransactionDate(ZonedDateTime.now());

		String accountObligatory = "origen y destino";

		final boolean destinyAccountExist = this.destiny != null;
		final boolean originAccountExist = this.origin != null;

		if(TransactionType.TRANSFERENCIA.equals(transactionType) && destinyAccountExist && originAccountExist) {

			if(this.destiny.getId().equals(this.origin.getId())) {
				throw new TransactionException(new StringBuilder()
						.append("Una transaccion de tipo: ").append(transactionType)
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

		final boolean transactionIsConsignacion = TransactionType.CONSIGNACION.equals(transactionType);
		if(transactionIsConsignacion && destinyAccountExist) {

			if(originAccountExist) {
				log.warn("validateCreation :: una {} no puede tener una cuenta de origen, borrando cuenta: {}",
						transactionType, this.origin);
				this.setOrigin(null);
			}
			this.destiny.addAmountToBalanceAccount(this.amount);
			return this;
		}
		else if(transactionIsConsignacion){
			accountObligatory = "destino";			
		}

		final boolean transactionIsRetiro = TransactionType.RETIRO.equals(transactionType);
		if(transactionIsRetiro && originAccountExist) {

			if(destinyAccountExist) {
				log.warn("validateCreation :: un {} no puede tener una cuenta de destino, borrando cuenta: {}",
						transactionType, this.destiny);
				this.setDestiny(null);
			}
			this.origin.substractAmountToBalanceAccount(this.amount);
			return this;
		}
		else if(transactionIsRetiro){
			accountObligatory = "origen";			
		}

		log.error("validateCreation :: transactionType: {}, accountObligatory: {}, destinyAccountExist: {}, originAccountExist: {}",
				transactionType, accountObligatory, destinyAccountExist, originAccountExist);
		throw new TransactionException("Las transaciones de tipo: " + transactionType.name() + " Deben contar con la cuenta de: " + accountObligatory);

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
