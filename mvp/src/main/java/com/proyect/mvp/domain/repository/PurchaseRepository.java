package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;

import reactor.core.publisher.Mono;

import java.util.List;


public interface PurchaseRepository extends R2dbcRepository<PurchaseEntity,UUID> {

    Mono<PurchaseEntity> findByMpPreferenceId(String mpPreferenceId);

}
