package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseDetailStateRepository extends R2dbcRepository<PurchaseDetailStateEntity, UUID> {

    @Query("INSERT INTO purchase_detail_state (id_purchase_detail_state, name) VALUES (:idPurchaseDetailState, :name)")
    Mono<PurchaseDetailStateEntity> insertPurchaseDetailState(UUID idPurchaseDetailState, String name);
}
