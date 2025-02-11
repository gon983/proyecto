package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.SaleEntity;

public interface SaleRepository extends R2dbcRepository<SaleEntity,String> {

}
