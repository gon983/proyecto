package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurchaseDetailRepository extends R2dbcRepository<PurchaseDetailEntity,UUID> {
    Flux<PurchaseDetailEntity>findAllByFkPurchase(UUID idPurchase);

    Mono<PurchaseDetailEntity> findByIdPurchaseDetail(UUID id);


    @Query("UPDATE purchase_detail SET quantity = :quantity WHERE id_purchase_detail = :idDetail")
    Mono<Void> updateQuantity(@Param("idDetail") UUID idDetail, @Param("quantity") double quantity);

    @Query("UPDATE purchase_detail SET fk_state = :idState WHERE id_purchase_detail = :idDetalle")
    Mono<Void> finalizarDetalle(@Param("idDetalle") UUID idDetail, @Param("idState") UUID idState);


    

    

}
