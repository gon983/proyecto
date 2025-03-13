package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.VoteEntity;

import reactor.core.publisher.Mono;

public interface VoteRepository extends R2dbcRepository<VoteEntity, UUID> {


    @Query("SELECT v.fkProduct FROM Vote v " +
       "WHERE v.fkStandarProduct = :idDefaultProductxCollectionPoint " +
       "GROUP BY v.fkProduct " +
       "ORDER BY COUNT(v.fkProduct) DESC " +
       "LIMIT 1")
    Mono<UUID> getMostVotedProductForDefaultProduct(@Param("idDefaultProductxCollectionPoint") UUID idDefaultProductxCollectionPoint);
    
}
