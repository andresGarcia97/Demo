package com.demo.advanced.service.mapper.client;

import com.demo.advanced.domain.Client;
import com.demo.advanced.entities.ClientEntity;
import com.demo.advanced.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper extends EntityMapper<Client, ClientEntity> {}
