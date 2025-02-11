package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.SaleStateEntity;

public interface SaleStateRepository extends R2dbcRepository<SaleStateEntity,String> {

}
