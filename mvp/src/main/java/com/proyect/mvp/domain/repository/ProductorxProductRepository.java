package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductorxProductEntity;

public interface ProductorxProductRepository extends R2dbcRepository<ProductorxProductEntity,String> {

}
