package com.demo.advanced.entities;

import com.demo.advanced.entities.enumeration.IdentificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "client")
public class ClientEntity {

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
	private IdentificationType type;

	@Column(name = "identification", nullable = false)
	private String identification;

	@Column(name = "name")
	private String name;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "born_date", nullable = false)
	private LocalDate bornDate;

	@Column(name = "creation_date", nullable = false)
	private ZonedDateTime creationDate;

	@Column(name = "modification_date")
	private ZonedDateTime modificationDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
	private Set<AccountBankEntity> accounts;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClientEntity)) {
			return false;
		}
		return getId() != null && getId().equals(((ClientEntity) o).getId());
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

}
