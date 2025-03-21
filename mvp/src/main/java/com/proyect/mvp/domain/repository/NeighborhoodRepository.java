package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NeighborhoodRepository extends R2dbcRepository<NeighborhoodEntity, UUID> {
    @Query("INSERT INTO neighborhood (id_neighborhood, name, fk_locality) VALUES (:idNeighborhood, :name, :fkLocality)")
    Mono<NeighborhoodEntity> insertNeighborhood(UUID idNeighborhood, String name, UUID fkLocality);

    @Query(value = "SELECT * FROM neighborhood WHERE fk_locality = :idLocality")
    Flux<NeighborhoodEntity> findByFkLocality(@Param("idLocality") UUID idLocality);

    
}