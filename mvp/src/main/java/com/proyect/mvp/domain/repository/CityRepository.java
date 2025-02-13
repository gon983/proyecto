package com.proyect.mvp.domain.repository;




import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CityEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface CityRepository extends R2dbcRepository<CityEntity,UUID> {

    Flux<CityEntity> findByCountryId(UUID countryId);

    Mono<CityEntity> insertCity(UUID idCity, String name, UUID countryId);


    

}
