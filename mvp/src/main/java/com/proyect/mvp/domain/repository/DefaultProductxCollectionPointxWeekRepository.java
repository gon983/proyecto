package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;

import reactor.core.publisher.Flux;

public interface DefaultProductxCollectionPointxWeekRepository extends R2dbcRepository<DefaultProductxCollectionPointxWeekEntity, UUID> {
    
    Flux<DefaultProductxCollectionPointxWeekEntity> findAllByFkCollectionPoint(UUID fkCollectionPoint);

}
