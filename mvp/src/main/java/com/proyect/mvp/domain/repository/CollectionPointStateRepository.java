package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointStateEntity;

public interface CollectionPointStateRepository extends R2dbcRepository<CollectionPointStateEntity,String> {

}
