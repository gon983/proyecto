package com.proyect.mvp.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;
import com.proyect.mvp.domain.model.entities.SaleEntity;

import reactor.core.publisher.Mono;

public interface SaleRepository extends R2dbcRepository<SaleEntity,UUID> {

    @Query(value = "SELECT p.*, SUM(s.quantity) AS totalQuantity, SUM(s.amount) AS totalAmount " +
                   "FROM sale s " +
                   "JOIN product p ON s.fk_product = p.id_product " +
                   "WHERE s.fk_collection_point = :collectionPoint " +
                   "GROUP BY s.fk_product, nativeQuery = true")
    Mono<List<SaleSummaryDTO>> getSalesSummary(@Param("collectionPoint") UUID collectionPoint);
}


