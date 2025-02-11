package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductStateEntity;

public interface ProductStateRepository extends R2dbcRepository<ProductStateEntity,String> {

}
