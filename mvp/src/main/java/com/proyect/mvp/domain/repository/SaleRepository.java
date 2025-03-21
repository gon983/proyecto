package com.proyect.mvp.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;


import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;

import com.proyect.mvp.domain.model.entities.SaleEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaleRepository extends R2dbcRepository<SaleEntity,UUID> {

    @Query("SELECT * from sale where fk_collection_point = :collectionPoint") 
    Flux<SaleEntity> getSalesForCollectionPoint(@Param("collectionPoint") UUID collectionPoint);

}


