package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurchaseDetailRepository extends R2dbcRepository<PurchaseDetailEntity,UUID> {
    Flux<PurchaseDetailEntity>findAllByFkPurchase(UUID idPurchase);

    Mono<PurchaseDetailEntity> findByIdPurchaseDetail(UUID id);

}
