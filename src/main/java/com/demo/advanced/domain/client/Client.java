package com.demo.advanced.domain.client;

import com.demo.advanced.entities.enumeration.IdentificationType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@Data
public class Client {

	private static final int MIN_YEARS = 18;
	private static final int MIN_LENGTH_NAME_AND_LAST_NAME = 2;
	private static final String REGEX_EMAIL = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";

	private Long id;

	private IdentificationType type;

	private String identification;

	private String name;

	private String lastName;

	private String email;

	private LocalDate bornDate;

	private ZonedDateTime creationDate;

	private ZonedDateTime modificationDate;

	public Client validateCreation() {

		this.validateBornDate();
		this.validateNameAndLastName();
		this.validateEmail();
		this.validateIdFields();

		if(this.modificationDate != null) {
			throw new ClientException("Un cliente nuevo, No puede tener fecha de modificación");
		}

		this.setCreationDate(ZonedDateTime.now().withNano(0));
		return this;
	}

	public Client validateUpdate(final Client client) throws ClientException {

		this.validateBornDate();
		this.validateNameAndLastName();
		this.validateEmail();
		this.validateIdFields();

		if(this.getCreationDate().toInstant().compareTo(client.getCreationDate().toInstant()) != 0) {
			log.error("validateUpdate :: dateCreationPrevious: {}, dateCreationUpdate: {}", client.getCreationDate(), this.getCreationDate());
			throw new ClientException("No se puede cambiar la fecha de creacion");
		}

		this.setModificationDate(ZonedDateTime.now());
		return this;
	}

	private void validateBornDate() throws ClientException {

		if(this.bornDate == null) {
			throw new ClientException("La fecha de nacimiento es obligatoria");
		}

		final LocalDate currentTime = LocalDate.now();

		if(this.bornDate.isAfter(currentTime)) {
			throw new ClientException("La fecha de nacimiento ingresada, es en el futuro");
		}

		final long diffYears = Math.abs(ChronoUnit.YEARS.between(currentTime, this.bornDate));
		if(diffYears < MIN_YEARS) {
			throw new ClientException("El usuario es menor de edad, tiene: " + diffYears + " años cuando lo minimo son: " + MIN_YEARS);
		}
	}

	private void validateNameAndLastName() throws ClientException {

		if(this.name == null || this.name.isBlank()) {
			throw new ClientException("El nombre es obligatorio");
		}

		if(this.lastName == null || this.lastName.isBlank()) {
			throw new ClientException("El apellido es obligatorio");
		}

		if(MIN_LENGTH_NAME_AND_LAST_NAME >= this.name.length() || MIN_LENGTH_NAME_AND_LAST_NAME >= this.lastName.length()) {
			throw new ClientException("El nombre y el apellido, deben de tener minimo " + MIN_LENGTH_NAME_AND_LAST_NAME + " caracteres");
		}
	}

	private void validateEmail() throws ClientException {

		if(this.email == null || this.email.isBlank()) {
			throw new ClientException("El correo es obligatorio");
		}

		if(!Pattern.matches(REGEX_EMAIL, this.email)) {
			throw new ClientException("El correo ingresado no es valido");
		}
	}

	private void validateIdFields() throws ClientException {

		if(this.identification == null || this.identification.isBlank()) {
			throw new ClientException("La identificación es obligatoria");
		}

		if(this.type == null) {
			throw new ClientException("El tipo de identificación es obligatoria, y solo puede ser de tipo: " +
					Arrays.toString(IdentificationType.values()));
		}
	}

}
