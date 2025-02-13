package com.proyect.mvp.domain.repository;




import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CityEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




public interface CityRepository extends R2dbcRepository<CityEntity,UUID> {

    Flux<CityEntity> findByCountryId(UUID countryId);

    @Query("INSERT INTO city (id_city, name, country_id) VALUES (:idCity, :name, :countryId)")
    Mono<CityEntity> insertCity(UUID idCity, String name, UUID countryId);


    

}
