package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductCategoryEntity;

public interface ProductCategoryRepository extends R2dbcRepository<ProductCategoryEntity,String> {

}
