package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;

public interface PurchaseRepository extends R2dbcRepository<PurchaseEntity,String> {

}
