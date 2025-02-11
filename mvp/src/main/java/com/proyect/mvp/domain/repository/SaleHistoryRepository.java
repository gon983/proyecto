package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.SaleHistoryEntity;

public interface SaleHistoryRepository extends R2dbcRepository<SaleHistoryEntity,String> {

}
