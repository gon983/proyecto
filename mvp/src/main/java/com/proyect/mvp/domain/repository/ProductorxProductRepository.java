package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.ProductorxProductEntity;

public interface ProductorxProductRepository extends R2dbcRepository<ProductorxProductEntity,String> {

}
