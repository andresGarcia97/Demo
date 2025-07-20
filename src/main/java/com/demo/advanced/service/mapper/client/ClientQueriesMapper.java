package com.demo.advanced.service.mapper.client;

import com.demo.advanced.dto.response.ClientResponse;
import com.demo.advanced.entities.ClientEntity;
import com.demo.advanced.service.mapper.QueriesMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientQueriesMapper extends QueriesMapper<ClientResponse, ClientEntity> {}
