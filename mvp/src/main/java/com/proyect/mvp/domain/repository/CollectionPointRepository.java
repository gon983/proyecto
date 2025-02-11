package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

public interface CollectionPointRepository extends R2dbcRepository<CollectionPointEntity,String> {

}
