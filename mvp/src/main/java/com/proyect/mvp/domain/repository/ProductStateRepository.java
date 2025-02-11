package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.ProductStateEntity;

public interface ProductStateRepository extends R2dbcRepository<ProductStateEntity,String> {

}
