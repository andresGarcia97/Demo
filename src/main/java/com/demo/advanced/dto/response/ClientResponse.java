package com.demo.advanced.dto.response;

import com.demo.advanced.entities.enumeration.IdentificationType;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record ClientResponse(Long id,
							 IdentificationType type,
							 String identification,
							 String name,
							 String lastName,
							 String email,
							 LocalDate bornDate,
							 ZonedDateTime creationDate,
							 ZonedDateTime modificationDate) {

}
