package com.demo.advanced.entities;

import com.demo.advanced.domain.enumeration.AccountState;
import com.demo.advanced.domain.enumeration.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "account_bank")
@NoArgsConstructor
public class AccountBankEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
	@SequenceGenerator(
			name = "sequence_generator",
			sequenceName = "sequence_generator",
			allocationSize = 10
	)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private AccountType type;

	@Column(name = "number", nullable = false, unique = true)
	private Long number;

	@Enumerated(EnumType.STRING)
	@Column(name = "state", nullable = false)
	private AccountState state;

	@Column(name = "balance", precision = 21, scale = 2, nullable = false)
	private BigDecimal balance;

	@Column(name = "creation_date", nullable = false)
	private ZonedDateTime creationDate;

	@Column(name = "modification_date")
	private ZonedDateTime modificationDate;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "client_id", referencedColumnName = "id")
	private ClientEntity client;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AccountBankEntity)) {
			return false;
		}
		return getId() != null && getId().equals(((AccountBankEntity) o).getId());
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

}
