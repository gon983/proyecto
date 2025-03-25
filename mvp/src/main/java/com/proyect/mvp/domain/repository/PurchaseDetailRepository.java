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

    @Query("SELECT * from purchase_detail where fk_collection_point = :collectionPoint and fk_productor = :fk_productor and fk_state = :fk_current_state") 
    Flux<PurchaseDetailEntity> getSalesPendingPaymentForCollectionPointAndProducer(@Param("collectionPoint") UUID collectionPoint, @Param("fk_productor") UUID fkProducer, @Param("fk_current_state") UUID fk_current_state);

    @Query("SELECT * from sale where fk_collection_point = :collectionPoint and fk_productor = :fk_productor and fk_state = :fk_current_state and fk_product = :fk_product") 
    Flux<PurchaseDetailEntity> getSalesPendingPaymentForProductAndCollectionPointAndProducer(@Param("collectionPoint") UUID collectionPoint, 
                                                                                @Param("fk_productor") UUID fkProducer, 
                                                                                @Param("fk_current_state") UUID fk_current_state,
                                                                                @Param("fk_product") UUID fkProduct);
    

}
