package com.demo.advanced.service.mapper.client;

import com.demo.advanced.domain.Client;
import com.demo.advanced.dto.request.ClientRequest;
import com.demo.advanced.service.mapper.DomainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientDomainMapper extends DomainMapper<ClientRequest, Client> {}
