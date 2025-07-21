package com.demo.advanced.dto.request;

import com.demo.advanced.domain.enumeration.IdentificationType;

import java.time.LocalDate;

public record ClientRequest(Long id,
                            IdentificationType type,
                            String identification,
                            String name,
                            String lastName,
                            String email,
                            LocalDate bornDate) {

}
