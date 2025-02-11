package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;

public interface PurchaseTypeRepository extends R2dbcRepository<PurchaseTypeEntity,String> {

}
