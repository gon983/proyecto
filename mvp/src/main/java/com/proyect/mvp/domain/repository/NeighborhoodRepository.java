package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NeighborhoodRepository extends R2dbcRepository<NeighborhoodEntity, UUID> {
    @Query("INSERT INTO neighborhood (id_neighborhood, name, fk_locality) VALUES (:idNeighborhood, :name, :localityId)")
    Mono<NeighborhoodEntity> insertNeighborhood(UUID idNeighborhood, String name, UUID localityId);

    Flux<NeighborhoodEntity> findByFkLocality(UUID id);

    
}