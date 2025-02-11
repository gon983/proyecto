package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductHistoryEntity;

public interface ProductHistoryRepository extends R2dbcRepository<ProductHistoryEntity,String> {

}
