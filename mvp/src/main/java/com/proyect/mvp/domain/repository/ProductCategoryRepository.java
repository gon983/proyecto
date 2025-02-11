package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.ProductCategoryEntity;

public interface ProductCategoryRepository extends R2dbcRepository<ProductCategoryEntity,String> {

}
