package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductEntity;

public interface ProductRepository extends R2dbcRepository<ProductEntity,String> {

}
