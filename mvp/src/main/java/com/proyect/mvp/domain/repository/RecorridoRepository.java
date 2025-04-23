package com.proyect.mvp.domain.repository;


import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import org.springframework.stereotype.Repository;

import com.proyect.mvp.domain.model.entities.RecorridoEntity;

import reactor.core.publisher.Flux;

@Repository
public interface RecorridoRepository extends R2dbcRepository<RecorridoEntity, UUID> {
    @Query("SELECT * FROM recorrido WHERE active = true")
    Flux<RecorridoEntity> findByActiveTrue();
}
