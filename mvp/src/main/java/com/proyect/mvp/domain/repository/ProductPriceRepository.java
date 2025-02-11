package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.ProductPriceEntity;

public interface ProductPriceRepository extends R2dbcRepository<ProductPriceEntity,String> {

}
