package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseTypeRepository extends R2dbcRepository<PurchaseTypeEntity, UUID> {

    @Query("INSERT INTO purchasetype (id_purchase_type, name) VALUES (:idPurchaseType, :name)")
    Mono<PurchaseTypeEntity> insertPurchaseType(UUID idPurchaseType, String name);
}