package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;

public interface PurchaseDetailRepository extends R2dbcRepository<PurchaseDetailEntity,String> {

}
