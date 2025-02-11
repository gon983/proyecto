package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.StockMovementEntity;

public interface StockMovementRepository extends R2dbcRepository<StockMovementEntity,String> {

}
