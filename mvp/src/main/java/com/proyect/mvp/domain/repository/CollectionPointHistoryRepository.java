package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;



public interface CollectionPointHistoryRepository extends R2dbcRepository<CollectionPointHistoryEntity,String> {

}
