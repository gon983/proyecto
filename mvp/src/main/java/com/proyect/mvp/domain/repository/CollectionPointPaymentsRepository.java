package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointPaymentsEntity;

public interface CollectionPointPaymentsRepository extends R2dbcRepository<CollectionPointPaymentsEntity,String> {

}
