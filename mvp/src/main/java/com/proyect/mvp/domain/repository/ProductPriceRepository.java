package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.ProductPriceEntity;

public interface ProductPriceRepository extends R2dbcRepository<ProductPriceEntity,String> {

}
