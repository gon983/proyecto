package com.proyect.mvp.domain.repository;


import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.CountryEntity;

import reactor.core.publisher.Mono;

public interface CountryRepository extends R2dbcRepository<CountryEntity,UUID> {
    @Query("INSERT INTO country (id_country, name) VALUES (:id, :name)")
    Mono<Void> insertCountry(@Param("id") UUID id, @Param("name") String name);

 }  


