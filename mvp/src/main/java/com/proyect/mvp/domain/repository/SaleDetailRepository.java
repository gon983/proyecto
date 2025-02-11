package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.SaleDetailEntity;

public interface SaleDetailRepository extends R2dbcRepository<SaleDetailEntity,String> {

}
