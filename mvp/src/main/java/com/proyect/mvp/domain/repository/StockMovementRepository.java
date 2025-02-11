package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.StockMovementEntity;

public interface StockMovementRepository extends R2dbcRepository<StockMovementEntity,String> {

}
