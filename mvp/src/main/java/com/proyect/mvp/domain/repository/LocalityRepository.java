package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.LocalityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LocalityRepository extends R2dbcRepository<LocalityEntity, UUID> {
    @Query("INSERT INTO locality (id_locality, name, fk_city) VALUES (:idLocality, :name, :cityId)")
    Mono<LocalityEntity> insertLocality(UUID idLocality, String name, UUID cityId);
}
