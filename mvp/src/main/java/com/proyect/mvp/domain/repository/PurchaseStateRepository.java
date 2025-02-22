package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseStateRepository extends R2dbcRepository<PurchaseStateEntity, UUID> {

    @Query("INSERT INTO purchase_state (id_purchase_state, name) VALUES (:idPurchaseState, :name)")
    Mono<PurchaseStateEntity> insertPurchaseState(UUID idPurchaseState, String name);
}