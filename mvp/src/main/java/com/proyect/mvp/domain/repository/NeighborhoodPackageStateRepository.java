package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NeighborhoodPackageStateRepository extends R2dbcRepository<NeighborhoodPackageStateEntity, UUID> {

    @Query("INSERT INTO neighborhoodpackagestate (id_neighborhood_package_state, name) VALUES (:idNeighborhoodPackageState, :name)")
    Mono<NeighborhoodPackageStateEntity> insertNeighborhoodPackageState(UUID idNeighborhoodPackageState, String name);
}