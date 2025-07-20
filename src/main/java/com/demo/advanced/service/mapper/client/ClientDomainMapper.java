package com.demo.advanced.service.mapper.client;

import com.demo.advanced.domain.client.Client;
import com.demo.advanced.dto.ClientDTO;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientDomainMapper extends DomainMapper<ClientDTO, Client> {}
