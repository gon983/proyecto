package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.StockMovementEntity;

import reactor.core.publisher.Mono;

public interface StockMovementRepository extends R2dbcRepository<StockMovementEntity,UUID> {
    

}
