package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.SaleStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SaleStateRepository extends R2dbcRepository<SaleStateEntity, UUID> {

    @Query("INSERT INTO salestate (id_sale_state, name) VALUES (:idSaleState, :name)")
    Mono<SaleStateEntity> insertSaleState(UUID idSaleState, String name);
}