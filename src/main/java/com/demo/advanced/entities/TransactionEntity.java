package com.demo.advanced.entities;

import com.demo.advanced.domain.enumeration.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "transaction")
@NoArgsConstructor
public class TransactionEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private TransactionType type;

	@Column(name = "date", nullable = false)
	private ZonedDateTime transactionDate;

	@Column(name = "amount", precision = 21, scale = 2, nullable = false)
	private BigDecimal amount;

	@JoinColumn
	@OneToOne(fetch = FetchType.EAGER)
	private AccountBankEntity origin;

	@JoinColumn
	@OneToOne(fetch = FetchType.EAGER)
	private AccountBankEntity destiny;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TransactionEntity)) {
			return false;
		}
		return getId() != null && getId().equals(((TransactionEntity) o).getId());
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

}
