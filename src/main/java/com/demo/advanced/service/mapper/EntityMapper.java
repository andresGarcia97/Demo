package com.demo.advanced.service.mapper;

/**
 * Contract for a generic Domain to Entity mapper.
 *
 * @param <D> - Domain type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper<D, E> {
	E toEntity(D dto);

	D toDomain(E entity);

}
