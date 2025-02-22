package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.CollectionPointStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CollectionPointStateRepository extends R2dbcRepository<CollectionPointStateEntity, UUID> {

    @Query("INSERT INTO collection_point_state (id_collection_point_state, name) VALUES (:idCollectionPointState, :name)")
    Mono<CollectionPointStateEntity> insertCollectionPointState(@Param("idCollectionPointState") UUID idCollectionPointState, @Param("name") String name);
}