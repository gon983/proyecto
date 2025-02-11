package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.StockMovementEntity;

public interface StockMovementRepository extends JpaRepository<StockMovementEntity,String> {

}
