package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.VoteEntity;

import reactor.core.publisher.Mono;

public interface VoteRepository extends R2dbcRepository<VoteEntity, UUID> {


    @Query("SELECT v.fk_product " +
       "FROM Vote v " +
       "WHERE v.fk_default_product_x_collection_point_x_week = :idDefaultProductxCollectionPoint " +
       "GROUP BY v.fk_product " +
       "ORDER BY COUNT(*) DESC " +  // Se usa COUNT(*) en lugar de COUNT(v.fkProduct)
       " LIMIT 1")  // En lugar de LIMIT 1 para compatibilidad
    Mono<UUID> getMostVotedProductForDefaultProduct(@Param("idDefaultProductxCollectionPoint") UUID idDefaultProductxCollectionPoint);

    
}
